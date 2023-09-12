package com.dasco.openhis.config.pay;

/**
 * @Author:
 */
public class AlipayConfig {

    //应用的ID，申请时的APPID
    public static String app_id = "2021000118647783";

    //SHA256withRsa对应支付宝公钥
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkloioiI10m9CGgEpxXtCXzOEQXb3Rm2xslqjc6iPJ0NDs5koXy8lFNm9n2iZAH7rSQjFKJxxs/e4uQrei535bIimsfVtEM2Wz7WVdkOHRGj7mQHZvJFVFhazBRxMqR3HagDCImCGG/+X6QKFFxfMxxYgWK33eLaTNeAXPsR3JhMxXc1NMu8XpZIcallX+8du1Rs63+k4mkGG1iuXafxA0magdFUMnx9CxzCZCd5+945fYRYVB7v2OaBCl3djLlusijj3OVWfEP8ZQ3DcQW38WXAPhacru+HBzxedgJGD6sxt3sZLt5Ki47djTlx5WDaH5hayhD/MC7sYhfnCLI3+JwIDAQAB";
    //签名方式
    public static String sign_type = "RSA2";
    //字码编码格式
    public static String charset = "utf-8";

    //回调地址
    public static String notifyUrl="http://45314rh250.qicp.vip/pay/callback/";

}
