package com.leyou.sms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ly.sms")
public class SmsProperty {
    private String secretId;
    private String secretKey;
    private String appId;
    private String appKey;
    private String signName;
    private String verifyCodeTemplate;
}
