package com.pavan.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pavan.model.UserBO;

@Repository
public interface UserRepository extends JpaRepository<UserBO, Long> {
	UserBO findByUsername(String username);
}
