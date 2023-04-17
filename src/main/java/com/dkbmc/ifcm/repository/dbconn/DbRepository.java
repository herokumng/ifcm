package com.dkbmc.ifcm.repository.dbconn;

import com.dkbmc.ifcm.domain.dbconn.DbEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * fileName     : DbRepository
 * author       : inayoon
 * date         : 2023-03-06
 * description  :
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-03-06       inayoon            최초생성
 */
public interface DbRepository extends JpaRepository<DbEntity, String> {
    DbEntity findAllById(String id);
}
