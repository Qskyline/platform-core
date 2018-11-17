package com.skyline.platform.core.dao;

import com.skyline.platform.core.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, String> {
	Role findByRoleName(String roleName);
}
