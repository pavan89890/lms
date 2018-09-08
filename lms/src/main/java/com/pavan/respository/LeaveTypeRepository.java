package com.pavan.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pavan.model.LeaveTypesBO;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveTypesBO, Long> {
}
