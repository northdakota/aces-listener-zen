package com.arkaces.aces_listener_zen.zen;

import lombok.Data;

@Data
public class ZenRpcResponse<T> {
    private T result;
    private Object error;
    private String id;
}
