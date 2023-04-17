package com.dkbmc.ifcm.module.api.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * fileName     : SfApiConfig
 * author       : inayoon
 * date         : 2023-03-15
 * description  : Salesforce API 테스트
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-03-15       inayoon            최초생성
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SfApiConfig {

    @Value("${system.client.id}")
    private String CLIENT_ID;

    @Value("${system.client.secret}")
    private String CLIENT_SECRET;

    @Value("${system.client.grantType}")
    private String GRANT_TYPE;

    @Value("${system.client.username}")
    private String USERNAME;

    @Value("${system.client.password}")
    private String PASSWORD;

    @Value("${token_url}")
    private String TOKEN_URL;

    private RestTemplate restTemplate;

    /**
     * callApi
     * @param  jsonObject, apiUrl, method
     * @return JSONObject
     */
    public JSONObject callApi(JSONObject jsonObject, String apiUrl, HttpMethod method) {
        RestTemplates restTemplates = RestTemplates.builder()
                .headers(tokenApi())
                .charset("UTF-8")
                .jsonObject(jsonObject)
                .build();
        try {
            return restTemplates.requestAsString2(apiUrl, method);
        } catch (Exception e) {
            log.error("callApi ERROR: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * TOKEN 발급
     * @param
     * @return String
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String tokenApi() {
        String token = "";

        try {
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            parameters.add("client_id", CLIENT_ID);
            parameters.add("client_secret", CLIENT_SECRET);
            parameters.add("grant_type", GRANT_TYPE);
            parameters.add("username", USERNAME);
            parameters.add("password", PASSWORD);

            HttpEntity requestEntity = new HttpEntity(parameters, headers);
            restTemplate = new RestTemplate();
            ResponseEntity<JSONObject> apiRes = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity, JSONObject.class);

            if (apiRes.getStatusCodeValue() == 200) {
                JSONObject obj = apiRes.getBody();
                token = obj.get("token_type").toString()+" "+obj.get("access_token").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("tokenApi ERROR:: {}", e.getMessage());
        }
        return token;
    }

}