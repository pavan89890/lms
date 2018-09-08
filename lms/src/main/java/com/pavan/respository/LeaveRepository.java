package com.pavan.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pavan.model.LeaveBO;
import com.pavan.model.UserBO;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveBO, Long> {
	List<LeaveBO> findByManagerAndStatus(UserBO manager, String status);

	List<LeaveBO> findByUserAndStatus(UserBO user, String status);

	@Query(value = "select sum(case when status='Requested' then 1 else 0 end) as 'Requested',sum(case when status='Rejected' then 1 else 0 end) as 'Rejected',sum(case when status='Approved' then 1 else 0 end) as 'Approved',sum(case when status='Cancelled' then 1 else 0 end) as 'Cancelled' from t_leave where user_id=:userId", nativeQuery = true)
	List<Object[]> leaveReport(@Param(value = "userId") long userId);
}
