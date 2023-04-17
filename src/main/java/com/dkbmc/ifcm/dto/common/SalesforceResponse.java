package com.dkbmc.ifcm.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

public class SalesforceResponse {

    @Data
    public static class Meta {
        private List<fields> fields;

        @Data
        public static class fields {
            private String name;
            private String type;


            @JsonProperty("length")
            private int max_length;

            @JsonProperty("nillable")
            private boolean nullable;

            private int precision;
            private int digits;
        }
    }

    @Data
    public static class Insert {
        private String id;
        private boolean success;
        private Object[] errors;
    }

    @Data
    public static class Upsert {
        private String id;
        private boolean success;
        private boolean created;
        private Object[] errors;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Token {

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("instance_url")
        private String instanceUrl;

        private String id;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("issued_at")
        private String issuedAt;

        private String signature;

        private String error;

        @JsonProperty("error_description")
        private String errorDescription;

        public String getSalesforceAuthToken() {
            return tokenType + " " + accessToken;
        }
    }
}
