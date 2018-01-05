package com.arkaces.aces_listener_zen.zen;

import lombok.Data;

import java.util.List;

@Data
public class ZenRpcRequest {
    private String jsonrpc = "1.0";
    private String id = "curltext";
    private String method;
    private List<Object> params;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }
}
