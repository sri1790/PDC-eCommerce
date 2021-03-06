package com.gaia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gaia.domain.UserMasterEntity;


public interface UserMasterRepo extends JpaRepository<UserMasterEntity, Long>, JpaSpecificationExecutor<UserMasterEntity> {

}
