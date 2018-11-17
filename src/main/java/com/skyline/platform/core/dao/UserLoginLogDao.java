package com.skyline.platform.core.dao;

import com.skyline.platform.core.entity.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginLogDao extends JpaRepository<UserLoginLog, String>  {
}
