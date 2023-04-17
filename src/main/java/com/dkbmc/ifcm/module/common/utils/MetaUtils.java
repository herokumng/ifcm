package com.dkbmc.ifcm.module.common.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dkbmc.ifcm.module.common.file.FileHandler;
import com.dkbmc.ifcm.module.common.file.FileTypeDef;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileContentType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileReadType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileUseType;
import com.dkbmc.ifcm.module.common.file.HandlerCreator;
import com.dkbmc.ifcm.module.common.parse.MetaParser;
import com.dkbmc.ifcm.module.core.ModuleConst;
import com.fasterxml.jackson.core.JsonProcessingException;

public class MetaUtils {
    private FileHandler handler;
    private StringBuilder builder;

    // Read Meta File
    public StringBuilder getMetaDataFromFile(String file_Path) throws IOException {
        handler = new HandlerCreator().create(FileType.META);
        handler.open(file_Path, FileUseType.READ);
        builder = handler.read(FileContentType.DATA, FileReadType.LOAD);
        handler.close(FileUseType.READ);
        return builder;
    }

    public <T> boolean setMetaData(String file_path, List<T> dataList) throws JsonProcessingException {
        handler = new HandlerCreator().create(FileType.META);
        handler.open(file_path, FileUseType.WRITE);
        boolean result = handler.write(FileContentType.DATA, FileTypeDef.FileWriteType.BULK, new MetaParser<T>().setMetaEntityEntries(dataList), ModuleConst.MODULE_DIGIT_0);
        handler.close(FileUseType.WRITE);
        return result;
    }


    public boolean delMetaData(String file_path) {
        handler = new HandlerCreator().create(FileType.META);
        handler.open(file_path, FileUseType.READ);
        return handler.delete(file_path);
    }

    public Class<?> getMetaEntityClass(String class_path) throws IOException, ClassNotFoundException {
        return Class.forName(class_path);
    }

    // load Entity Class
    @SuppressWarnings("unchecked")
    public <T> T getMetaEntity(String class_path)
            throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<T> entity = (Class<T>) Class.forName(class_path);
        return entity.getDeclaredConstructor().newInstance();
    }

    // get Data Row from entity List
    public <T> Object getMetaEntityData(List<T> data_list, String find_key, Object find_value)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException, InstantiationException {
        Object dataRow = null;
        for (T element : data_list) {
            Map<String, Object> mapData = covertEntityToMap(element);
            if (mapData.get(find_key).equals(find_value)) {
                dataRow = element;
            }
        }
        return (dataRow == null) ? null : dataRow;
    }

    // get Entity Item Value from Meta Entity Data
    public <T> Object getMetaEntityItemValue(T meta_entity_data, String item_name)
            throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> mapData = covertEntityToMap(meta_entity_data);
        if (mapData.containsKey(item_name)) {
            return mapData.get(item_name) == null ? null : mapData.get(item_name);
        } else {
            return null;
        }
    }


    // get Entity Item Value from Meta Entity Data list by Multi Condition
    public <T> Object getMetaEntityItemValue(List<T> data_list, Map<String, Object> search_condition, String item_name)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,
            SecurityException, InstantiationException {
        Object itemValue = null;
        for (T element : data_list) {

            Map<String, Object> mapData = covertEntityToMap(element);
            int matchCnt = ModuleConst.MODULE_DIGIT_0;

            for (String findKey : search_condition.keySet()) {
                if (mapData.get(findKey).equals(search_condition.get(findKey))) {
                    matchCnt++;
                }
            }

            if (matchCnt == search_condition.size()) {
                if (mapData.containsKey(item_name)) {
                    itemValue = mapData.get(item_name) == null ? null : mapData.get(item_name);
                }
                break;
            }
        }
        return itemValue;
    }


    // get Entity Item Value from Meta Entity Data list
    public <T> Object getMetaEntityItemValue(List<T> data_list, String find_key, Object find_value, String item_name)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,
            SecurityException, InstantiationException {
        return getMetaEntityItemValue(getMetaEntityData(data_list, find_key, find_value), item_name);
    }

    // Transform Entity -> Map Object
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> Map<String, Object> covertEntityToMap(T entity) throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> mapEntity = new LinkedHashMap<>();
        Field[] fields = entity.getClass().getDeclaredFields();
        List<Field> totalFields = new ArrayList(Arrays.asList(fields));
        fields = entity.getClass().getSuperclass().getDeclaredFields();
        totalFields.addAll(Arrays.asList(fields));

        for (Field field : totalFields) {
            field.setAccessible(true);

//			if (field.getType().equals(LocalDateTime.class)) {
//				String datatime = field.get(entity).toString();
//				mapEntity.put(field.getName(), (Object) datatime);
//			} else {
            mapEntity.put(field.getName(), field.get(entity));
//			}
        }
        return mapEntity;
    }

    public <T> List<Map<String, Object>> convertListEntityToListMap(List<T> listEntity) throws IllegalAccessException {
        List<Map<String, Object>> listMapEntity = new ArrayList<>();
        for (T entity : listEntity) {
            listMapEntity.add(covertEntityToMap(entity));
        }
        return listMapEntity;
    }

    /**
     * 리스트에서 지정 항복을 지운다.
     *
     * @param original 오리지날 리스트 소스
     * @param id       지워질 리스트 항목의 id
     * @return 지워진 리스트
     */
    public <T> List<Map<String, Object>> deleteList(List<T> original, String id) {   //  추후 델리스트 리스트 type  enum 생성 해야함

        List<Map<String, Object>> deleteItemList;
        List<Map<String, Object>> modifiedList;
        try {
            modifiedList = convertListEntityToListMap(original);
            deleteItemList = modifiedList.stream()
                    .filter(list -> list.containsKey("map_id") ? list.get("map_id").equals(id) : list.get("id").equals(id))
                    .collect(Collectors.toList());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (modifiedList.removeAll(deleteItemList)) {
            return modifiedList;
        } else {
            throw new IllegalArgumentException("맵핑 리스트 삭제에 실패하였습니다");
        }
    }

}