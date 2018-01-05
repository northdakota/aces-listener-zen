package com.arkaces.aces_listener_zen.zen;

import com.arkaces.aces_server.common.json.NiceObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ZenService {

    private final RestTemplate zenRpcRestTemplate;

    private final ZenRpcRequestFactory zenRpcRequestFactory = new ZenRpcRequestFactory();

    private final NiceObjectMapper objectMapper = new NiceObjectMapper(new ObjectMapper());

    public List<JsonNode> getTransactions(Integer blockDepth) {
        HttpEntity<String> blockHashRequestEntity = getRequestEntity("getbestblockhash", new ArrayList<>());
        String blockHash = zenRpcRestTemplate
                .exchange(
                    "/",
                    HttpMethod.POST,
                    blockHashRequestEntity,
                    new ParameterizedTypeReference<ZenRpcResponse<String>>() {}
                )
                .getBody()
                .getResult();

        HttpEntity<String> blockRequestEntity = getRequestEntity("getblock", Arrays.asList(blockHash));
        Block block = zenRpcRestTemplate
                .exchange(
                        "/",
                        HttpMethod.POST,
                        blockRequestEntity,
                        new ParameterizedTypeReference<ZenRpcResponse<Block>>() {}
                )
                .getBody()
                .getResult();

        log.info("Processing transactions in block (n = " + block.tx.size() + ")");

        List<JsonNode> transactions = new ArrayList<>();
        for (String transactionId : block.tx) {
            log.info("getting raw transaction for transactionId = " + transactionId);
            HttpEntity<String> transactionRequestEntity = getRequestEntity(
                    "getrawtransaction",
                    Arrays.asList(transactionId, true)
            );
            try {
                JsonNode transaction = zenRpcRestTemplate
                        .exchange(
                                "/",
                                HttpMethod.POST,
                                transactionRequestEntity,
                                JsonNode.class
                        )
                        .getBody()
                        .get("result");
                transactions.add(transaction);
            } catch (HttpServerErrorException e) {
                log.warn("Failed to get transaction data" + e.getResponseBodyAsString());
            } catch (Exception e) {
                log.warn("Failed to extract transaction data", e);
            }
        }

        // scan blocks recursively until blockDepth is reached
        if (! blockDepth.equals(0)) {
            transactions.addAll(getTransactions(blockDepth - 1));
        }
        return transactions;
    }

    private HttpEntity<String> getRequestEntity(String method, List<Object> params) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "text/plain");

        ZenRpcRequest zenRpcRequest = zenRpcRequestFactory.create(method, params);
        String body = objectMapper.writeValueAsString(zenRpcRequest);

        return new HttpEntity<>(body, headers);
    }

}
