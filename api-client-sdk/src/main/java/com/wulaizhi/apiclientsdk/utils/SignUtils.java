package com.wulaizhi.apiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class SignUtils {
    public static String genSign(String body,String secretKey) {
        Digester sha = new Digester(DigestAlgorithm.SHA256);
        String contennt = body+"."+secretKey;
        return sha.digestHex(contennt);
    }
}
