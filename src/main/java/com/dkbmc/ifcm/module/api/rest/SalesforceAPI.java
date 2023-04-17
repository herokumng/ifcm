package com.dkbmc.ifcm.module.api.rest;

import com.dkbmc.ifcm.dto.common.SalesforceRequest;
import com.dkbmc.ifcm.dto.common.SalesforceResponse;
import com.dkbmc.ifcm.module.core.ModuleConst;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SalesforceAPI {
    WebClient webClient;
    final ObjectMapper objectMapper;

    private Map<String, String> requestSystem;
    private final Map<String, String> attributes = new HashMap<>();
    SalesforceRequest.Records records = new SalesforceRequest.Records();

    public SalesforceAPI(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Salesforce api 접속을위한 access 정보를 받기위한 api
     *
     * @param tgtSys 타겟 시스템 정보
     *               Method Type = POST
     * @return access 정보
     */
    public SalesforceResponse.Token GetToken(List<Map<String, String>> tgtSys) {
        requestSystem = tgtSys.get(ModuleConst.MODULE_DIGIT_0);
        webClient = WebClient.create(requestSystem.get("domain") + requestSystem.get("path"));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.setAll(requestSystem);
        return webClient.post()
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToFlux(SalesforceResponse.Token.class)
                .blockLast();
    }

    /**
     * 메다데이터 로드 api
     *
     * @param token      api접속 정보를 받아온 dto.
     * @param tgtObjName 타겟 오브젝트 네임
     *                   Method Type = GET
     * @return 받아온 메타데이터.
     */
    public List<SalesforceResponse.Meta.fields> GetMeta(SalesforceResponse.Token token, List<Map<String, String>> tgtSys, String tgtObjName) {
        requestSystem = tgtSys.get(ModuleConst.MODULE_DIGIT_1);
        webClient = WebClient.create(requestSystem.get("domain") + requestSystem.get("path") + tgtObjName + "/describe");
        
        return Objects.requireNonNull(webClient.get()
                        .header("Authorization", token.getSalesforceAuthToken())
                        .retrieve()
                        .bodyToFlux(SalesforceResponse.Meta.class)
                        .blockLast())
                		.getFields();
    }

    /**
     * 대량의 레코드 Upsert Salesforce rest api
     *
     * @param token      타겟오브젝트의 접속 인증 토큰
     * @param tgtSys     타겟 시스템
     * @param sObject    타겟 오브젝트
     * @param extIdField 타겟 오브젝트 External Field
     * @param params     upsert 파라미터
     * @return 성공여부
     */
//    public List<SalesforceResponse.Upsert> CompositeUpsert(SalesforceResponse.Token token // 추후 실패한 레코드 저장시 변경 가능
    public Boolean CompositeUpsert(SalesforceResponse.Token token
            , List<Map<String, String>> tgtSys
            , String sObject
            , String extIdField
            , List<Map<String, Object>> params) {
        attributes.put("type", sObject);
        params.forEach(param -> param.put("attributes", attributes));

        requestSystem = tgtSys.get(ModuleConst.MODULE_DIGIT_3);
        records.setAllOrNone(false);
        records.setRecords(params);

        webClient = WebClient.builder()
                .baseUrl(requestSystem.get("domain") + requestSystem.get("path") + sObject + "/" + extIdField)
                .defaultHeader("Authorization", token.getSalesforceAuthToken())
                .build();

        Flux<SalesforceResponse.Upsert> response;
        response = webClient.patch()
                .body(BodyInserters.fromValue(objectMapper.convertValue(records, JSONObject.class)))
                .retrieve()
                .bodyToFlux(SalesforceResponse.Upsert.class);

        List<SalesforceResponse.Upsert> responseList = response.collect(Collectors.toList()).share().block();

        //실패한 레코드를 정리하는 기능 추후에 사용이 필요할 수도있습니다.
        /*int index = 0;
        for (SalesforceResponse.Insert Insert : responseList) {
            params.get(index).remove("attributes");
            Insert.setRecords(params.get(index));
            index++;
        }*/
        //실패한 레코드
        return !(Objects.requireNonNull(responseList).stream().filter(result -> !result.isSuccess()).toList().size() > 0);
    }

    /**
     * 대량의 레코드 Insert Salesforce rest api
     *
     * @param token   타겟오브젝트의 접속 인증 토큰
     * @param tgtSys  타겟 시스템
     * @param sObject 타겟 오브젝트
     * @param params  insert 파라미터
     * @return 성공여부
     */
    //    public List<SalesforceResponse.Insert> CompositeInsert(SalesforceResponse.Token responseDTO  // 추후 실패한 레코드 저장시 변경 가능
    public Boolean CompositeInsert(SalesforceResponse.Token token
            , List<Map<String, String>> tgtSys
            , String sObject
            , List<Map<String, Object>> params) {

        if (sObject != null && !sObject.equals("")) {
            attributes.put("type", sObject);
        }
        params.forEach(param -> param.put("attributes", attributes));

        requestSystem = tgtSys.get(ModuleConst.MODULE_DIGIT_2);
        records.setAllOrNone(true);
        records.setRecords(params);

        webClient = WebClient.builder()
                .baseUrl(requestSystem.get("domain") + requestSystem.get("path"))
                .defaultHeader("Authorization", token.getSalesforceAuthToken())
                .build();
        Flux<SalesforceResponse.Insert> response;
        response = webClient.post()
                .body(BodyInserters.fromValue(objectMapper.convertValue(records, JSONObject.class)))
                .retrieve()
                .bodyToFlux(SalesforceResponse.Insert.class);
        List<SalesforceResponse.Insert> responseList = response.collect(Collectors.toList()).share().block();
        
        //실패한 레코드를 정리하는 기능 추후에 사용이 필요할 수도있습니다.
        /*
        int index = 0;
        for (SalesforceResponse.Insert Insert : responseList) {
            params.get(index).remove("attributes");
            Insert.setRecords(params.get(index));
            index++;
        }
        */
        //실패한 레코드
        return !(Objects.requireNonNull(responseList).stream().filter(result -> !result.isSuccess()).toList().size() > 0);
    }

}