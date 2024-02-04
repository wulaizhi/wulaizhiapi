package com.wulaizhi.apigateway.filter;
import com.wulaizhi.springbootinit.model.entity.InterfaceInfo;
import com.wulaizhi.springbootinit.model.entity.User;
import com.wulaizhi.springbootinit.service.UserInterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import static com.wulaizhi.apiclientsdk.utils.SignUtils.genSign;
import static com.wulaizhi.apigateway.enums.AuthEnum.*;


@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    private static final List<String> WHITE_LIST = List.of("127.0.0.1");
    private  static User user;

    private InterfaceInfo interfaceInfo;
    @DubboReference
    UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response =exchange.getResponse();
        //输出请求日志
        log.info("begin global PrintLogFilter");
        //1.请求日志
        log.info("请求唯一标识:" + request.getId());
        String path = request.getPath().value();
        log.info("请求路径:" + path);
        String method = String.valueOf(request.getMethod());
        log.info("请求方法:" + method);
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        log.info("请求参数:" + queryParams);
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址:" + sourceAddress);
        log.info("远程地址:" + request.getRemoteAddress());
        URI uri = request.getURI();
        //5,请求的模拟接口是否存在
        String s = uri.toString().split("\\?")[0];
        interfaceInfo = userInterfaceInfoService.invokeInterface(s, method);
        if (interfaceInfo==null) {
            return handleInvokeError(response);
        }
        //3,黑白名单
        if (sourceAddress == null || !WHITE_LIST.contains(sourceAddress)) {
            return handleNoAuth(response);
        }
        //鉴权
        if (!authInfo(request)) {
            return handleNoAuth(response);
        }
        return enhanceResponse(exchange, chain);
    }

    @Override
    public int getOrder() {
        return -1;
    }
    public boolean authInfo(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst(ACCESSKEY.getValue());
        String nonce = headers.getFirst(NONCE.getValue());
        String body = new String(headers.getFirst(BODY.getValue()).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        String timestamp = headers.getFirst(TIMESTAMP.getValue());
        String sign = headers.getFirst(SIGN.getValue());
        //4,用户鉴权(判断,ak,sk,是否合法)
        if (StringUtils.isAnyEmpty(accessKey, nonce, timestamp, sign)) {
            return false;
        }
        //accessKey校验是否已经分配
        user = userInterfaceInfoService.invokeUserInfo(accessKey);
        if (user==null) {
            return false;
        }
        String realAccessKey = user.getAccessKey();
        if (!accessKey.equals(realAccessKey)) {
            return false;
        }
        //校验随机数的长度，通常要服务端记录随机数，来防止重放攻击
        if (Long.parseLong(nonce) > 10000) {
            return false;
        }
        //校验timestamp 的时效性，通常指定过期时间比如1分钟，过期了就无权限。
        Long currentTime = System.currentTimeMillis() / 1000;
        Long FIVE_MINUTES = 60 * 5L;
        if (timestamp == null || currentTime - Long.parseLong(timestamp) > FIVE_MINUTES) {
            return false;
        }

        //验证签名的有效性，根据签名accessKey查询secretKey
        String realSecretKey = user.getSecretKey();
        String genSign = genSign(body, realSecretKey);
        if (!sign.equals(genSign)) {
            return false;
        }
        //校验是否有次数剩余

        return true;
    }


    public Mono<Void> enhanceResponse(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                try {
                                   userInterfaceInfoService.invokeCount(user.getId(), interfaceInfo.getId());
                                }catch (Exception e) {
                                   log.error("invokeCount()"+"服务器内部异常,调用失败!");
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);
                                StringBuilder sb2 = new StringBuilder(200);
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                String data = new String(content, StandardCharsets.UTF_8);
                                sb2.append(data);
                                log.info("响应结果:" + data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
}

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}
