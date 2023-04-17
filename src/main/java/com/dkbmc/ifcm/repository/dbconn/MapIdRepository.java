package com.dkbmc.ifcm.repository.dbconn;

import com.dkbmc.ifcm.domain.dbconn.MapIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * fileName     : MapIdRepository
 * author       : inayoon
 * date         : 2023-03-28
 * description  :
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-03-28       inayoon            최초생성
 */
public interface MapIdRepository extends JpaRepository<MapIdEntity, Integer> {
    List<MapIdEntity> findAllBySourceObj(String sourceObj);
}
