package com.skyline.platform.core.dao;

import com.skyline.platform.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, String>  {
	User findByUsername(String username);
	List<User> findByMobilephoneNumber(String mobilephoneNumber);
	List<User> findByUsernameAndPassword(String username, String password);
}
