package com.dasco.openhis.config;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    /**
     * url前半部分
     */
    @Value("${api_url}")
    String BASE_URL;

    /**
     * 开发者注册后系统自动生成的账号，可在官网登录后查看
     */
    @Value("${account_sid}")
    String ACCOUNT_SID;

    /**
     * 开发者注册后系统自动生成的TOKEN，可在官网登录后查看
     */
    @Value("${auth_token}")
    String AUTH_TOKEN;

    /**
     * 响应数据类型, JSON或XML
     */
    @Value("${resp_data_type}")
    String RESP_DATA_TYPE;

    /**
     * 模板id
     */
    @Value("${template_id}")
    String TEMPLATE_ID;

    public String getBASE_URL() {
        return BASE_URL;
    }

    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public String getACCOUNT_SID() {
        return ACCOUNT_SID;
    }

    public void setACCOUNT_SID(String ACCOUNT_SID) {
        this.ACCOUNT_SID = ACCOUNT_SID;
    }

    public String getAUTH_TOKEN() {
        return AUTH_TOKEN;
    }

    public void setAUTH_TOKEN(String AUTH_TOKEN) {
        this.AUTH_TOKEN = AUTH_TOKEN;
    }

    public String getRESP_DATA_TYPE() {
        return RESP_DATA_TYPE;
    }

    public void setRESP_DATA_TYPE(String RESP_DATA_TYPE) {
        this.RESP_DATA_TYPE = RESP_DATA_TYPE;
    }

    public String getTEMPLATE_ID() {
        return TEMPLATE_ID;
    }

    public void setTEMPLATE_ID(String TEMPLATE_ID) {
        this.TEMPLATE_ID = TEMPLATE_ID;
    }

    @Override
    public String toString() {
        return "SmsSendConfig{" +
                "BASE_URL='" + BASE_URL + '\'' +
                ", ACCOUNT_SID='" + ACCOUNT_SID + '\'' +
                ", AUTH_TOKEN='" + AUTH_TOKEN + '\'' +
                ", RESP_DATA_TYPE='" + RESP_DATA_TYPE + '\'' +
                ", TEMPLATE_ID='" + TEMPLATE_ID + '\'' +
                '}';
    }

    /**
     * 构造通用参数timestamp、sig和respDataType
     *
     * @return
     */
    public String createCommonParam(String sid,String token) {
        // 时间戳
        long timestamp = System.currentTimeMillis();
        // 签名
        String sig = DigestUtils.md5Hex(sid + token + timestamp);

        return "&timestamp=" + timestamp + "&sig=" + sig + "&respDataType=" + getRESP_DATA_TYPE();
    }
}
