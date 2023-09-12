package com.dasco.openhis.config.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

    private String hashAlogrithmName = "md5";

    private Integer hashIterators = 2;
    //放行的路径
    private String [] anonUrls;
    //拦截的路径
    private String [] authcUrls;
}
