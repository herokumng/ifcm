package com.dkbmc.ifcm.module.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * fileName     : RestTemplates
 * author       : inayoon
 * date         : 2023-03-16
 * description  : RestAPI를 통해 정보를 request하는 Class
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-03-16       inayoon            최초생성
 */
@Slf4j
public class RestTemplates {
    private HttpHeaders headers;
    private Charset charset;
    private JSONObject jsonObject;

    public RestTemplates() {
    }

    public RestTemplates(RestemplateBuilder builder) {
        this.headers = builder.headers;
        this.charset = builder.charset;
        this.jsonObject = builder.jsonObject;
    }

    public static RestemplateBuilder builder() {
        return new RestemplateBuilder();
    }

    public static RestemplateBuilder builder(HttpHeaders headers, Charset charset, JSONObject jsonObject) {
        return new RestemplateBuilder(headers, charset, jsonObject);
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public HttpEntity getRequestEntity() throws Exception {
        String parameters = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            parameters = objectMapper.writeValueAsString(this.jsonObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new Exception("JsonProcessingException");
        }
        HttpEntity requestEntity = new HttpEntity(parameters, headers);
        return requestEntity;
    }

    /**
     * BLOB 값으로 response를 받음
     * @param  apiUrl 호출 url
     * @param  method GET, PUT, DELETE, POST
     * @return 필요에 따라 추가
     * @throws Exception
     */
    public Map<String, Object> requestAsBLOB(String apiUrl, HttpMethod method) throws Exception {
        Map<String, Object> res = new HashMap<>();
        org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
        ResponseEntity<byte[]> apiResponse = restTemplate.exchange(apiUrl, method, this.getRequestEntity(), byte[].class);
        if ( apiResponse.getStatusCodeValue() == 200 ) {
            res.put("errorCode", "200");
            res.put("body", apiResponse.getBody());
            res.put("header", apiResponse.getHeaders());
        }
        return res;
    }

    /**
     * String 값으로 response를 받음
     * @param  apiUrl 호출 url
     * @param  method GET, PUT, DELETE, POST
     * @return 필요에 따라 추가
     * @throws Exception
     */
    public Map<String, Object> requestAsString(String apiUrl, HttpMethod method) throws Exception {
        Map<String, Object> res = new HashMap<>();
        org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
        ResponseEntity<String> apiResponse = restTemplate.exchange(apiUrl, method, this.getRequestEntity(), String.class);

        if ( apiResponse.getStatusCodeValue() == 200 ) {
            res.put("errorCode", "200");
            res.put("body", apiResponse.getBody());
            res.put("header", apiResponse.getHeaders());
        }
        return res;
    }

    /**
     * requestAsString2()
     * String 값으로 response를 받음
     * @param  apiUrl 호출 url
     * @param  method GET, PUT, DELETE, POST
     * @return 필요에 따라 추가
     * @throws Exception
     */
    public JSONObject requestAsString2(String apiUrl, HttpMethod method) throws Exception {
        ClientHttpRequestFactory httpRequestFactory =  new HttpComponentsClientHttpRequestFactory();
        org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate(httpRequestFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        ResponseEntity<String> apiResponse = restTemplate.exchange(apiUrl, method, this.getRequestEntity(), String.class);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = null;
        if ( apiResponse.getStatusCodeValue() == 200 ) {
            Object obj = jsonParser.parse(apiResponse.getBody());
            jsonObj = (JSONObject) obj;
        }
        return jsonObj;
    }


    public static class RestemplateBuilder {
        private HttpHeaders headers;
        private Charset charset;
        private JSONObject jsonObject;

        public RestemplateBuilder() {
        }

        public RestemplateBuilder(HttpHeaders headers, Charset charset, JSONObject jsonObject) {
            this.headers = headers;
            this.charset = charset;
            this.jsonObject = jsonObject;
        }

        public RestemplateBuilder headers(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public RestemplateBuilder headers(String token) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            this.headers = headers;
            return this;
        }

        public RestemplateBuilder headers(String token, String contentType) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            headers.add("Content-Type", contentType);
            this.headers = headers;
            return this;
        }

        public RestemplateBuilder charset(String charsetName) {
            this.charset = Charset.forName(charsetName);
            return this;
        }

        public RestemplateBuilder jsonObject(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
            return this;
        }

        public RestTemplates build() {
            return new RestTemplates(this);
        }
    }

}