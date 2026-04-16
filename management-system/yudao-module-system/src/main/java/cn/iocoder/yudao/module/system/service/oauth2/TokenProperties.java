package cn.iocoder.yudao.module.system.service.oauth2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "yudao.token")
@Data
public class TokenProperties {

    @NotNull(message = "访问令牌过期时间不能为空")
    private Long accessTokenExpireSeconds = 86400L;

    @NotNull(message = "刷新令牌过期时间不能为空")
    private Long refreshTokenExpireSeconds = 2592000L;

}
