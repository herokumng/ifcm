package com.dkbmc.ifcm.module.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * fileName     : CommonUtils
 * author       : inayoon
 * date         : 2023-04-10
 * description  : 공통 Util 정보
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-04-10       inayoon            최초생성
 */
@Slf4j
public class CommonUtils {

    /**
     * 특정 Key 중복제거
     * @param  <T>
     * @param  keyExtractor
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
