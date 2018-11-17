package com.skyline.platform.core.dao;

import com.skyline.platform.core.entity.User;
import com.skyline.platform.core.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleDao extends JpaRepository<UserRole, String> {
	List<UserRole> findByUser(User user);
}