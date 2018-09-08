package com.pavan.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pavan.model.LeaveDatesBO;

@Repository
public interface LeaveDatesRepository extends JpaRepository<LeaveDatesBO, Long> {
}
