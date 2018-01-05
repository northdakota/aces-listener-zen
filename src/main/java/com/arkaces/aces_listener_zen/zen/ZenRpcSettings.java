package com.arkaces.aces_listener_zen.zen;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "zenRpc")
public class ZenRpcSettings {
    private String username;
    private String password;
}
