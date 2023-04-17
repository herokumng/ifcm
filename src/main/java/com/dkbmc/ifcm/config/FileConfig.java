package com.dkbmc.ifcm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * fileName     : FileConfig
 * author       : inayoon
 * date         : 2023-02-28
 * description  : DB 접속정보 파일 생성
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-02-28       inayoon            최초생성
 */
@Slf4j
public class FileConfig {
    String filePath = "./meta/db/dbConnData";

    /**
     * 파일 읽기
     * @param  filePath
     * @return String
     */
    public String readFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + System.lineSeparator());
        }
        return sb.toString();
    }


    /**
     * 파일 생성
     * @param  request
     * @return
     */
    public void createFile(HashMap<String, String> request) throws IOException, ParseException {
        File file = new File(filePath);
        if(!file.exists()){   // 파일 신규생성
            file.createNewFile();
        }

        String content = "";
        content = readFile(filePath);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("db_driver", request.get("driver"));
        map.put("db_host", request.get("host"));
        map.put("db_port", request.get("port"));
        map.put("db", request.get("db"));
        map.put("db_user", request.get("user"));
        map.put("db_pw", request.get("pw"));
        map.put("sys_type", request.get("sysType"));

        JSONArray jsonArray = new JSONArray();
        if(content != null && !content.equals("")) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(content);
            jsonArray = (JSONArray) jsonObj.get("data_list");
        }
        jsonArray.add(map);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data_list", jsonArray);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);

        BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
        bw.write(result);
        bw.newLine();

        // 버퍼 및 스트림 종료
        bw.flush();
        bw.close();
    }


    /**
     * 연동시스템명 리스트 조회
     * @param
     * @return List<String>
     */
    public List<String> getSysTypeList() throws IOException, ParseException {
        List<String> sysTypeList = new ArrayList<String>();

        File file = new File(filePath);
        if(!file.exists()){
            file.createNewFile();
        }

        String content = "";
        content = readFile(filePath);

        JSONArray jsonArray = new JSONArray();
        if(content != null && !content.equals("")) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(content);
            jsonArray = (JSONArray) jsonObject.get("data_list");
        }

        for(int i=0; i<jsonArray.size(); i++) {
            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
            String sys_type = (String) jsonObj.get("sys_type");
            sysTypeList.add(sys_type);
        }
        return sysTypeList;
    }

    /**
     * 연동시스템 DB접속정보 조회
     * @param  systemType
     * @return JSONObject
     */
    public JSONObject getSysTypeData(String systemType) throws IOException, ParseException {
        JSONObject jsonObj = new JSONObject();
        String content = "";
        content = readFile(filePath);

        JSONArray jsonArray = new JSONArray();
        if(content != null && !content.equals("")) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(content);
            jsonArray = (JSONArray) jsonObject.get("data_list");
        }

        for(int i=0; i<jsonArray.size(); i++) {
            jsonObj = (JSONObject) jsonArray.get(i);
            String sys_type = (String) jsonObj.get("sys_type");
            if (sys_type.equals(systemType)) {
                break;
            }
        }
        return jsonObj;
    }

}
