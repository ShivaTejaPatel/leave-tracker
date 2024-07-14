package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.LeaveApplication;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication,Long> {

	
	Optional<LeaveApplication> findByIdAndEmployeeId(Long leaveId, Long employeeId);
}

