package com.dkbmc.ifcm.dto.common;

import lombok.Data;

import java.util.List;
import java.util.Map;


public class SalesforceRequest {
    @Data
    public static class Records {
        private boolean allOrNone;
        private List<Map<String, Object>> records;
    }

    @Data
    public static class Token {
        private String grant_type = "password";
        private String username;
        private String password;
        private String client_id;
        private String client_secret;
    }
}
