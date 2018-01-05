package com.arkaces.aces_listener_zen.zen;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ZenRpcRequestFactory {

    public ZenRpcRequest create(String method) {
        return create(method, new ArrayList<>());
    }

    public ZenRpcRequest create(String method, List<Object> params) {
        ZenRpcRequest request = new ZenRpcRequest();
        request.setMethod(method);
        request.setParams(params);
        return request;
    }
}
