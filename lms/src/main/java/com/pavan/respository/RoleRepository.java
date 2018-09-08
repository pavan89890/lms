package com.pavan.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pavan.model.RoleBO;

@Repository
public interface RoleRepository extends JpaRepository<RoleBO, Long> {
	public RoleBO findByRolename(String rolename);
}
