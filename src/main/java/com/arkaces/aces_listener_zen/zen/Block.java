package com.arkaces.aces_listener_zen.zen;

import lombok.Data;

import java.util.List;

@Data
public class Block {
    String hash;
    Integer confirmations;
    List<String> tx;
    String previousblockhash;
}