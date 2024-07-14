package com.example.demo.service;

import com.example.demo.payload.LeaveApplicationDto;
import com.example.demo.payload.LeaveApplicationStatusDto;

public interface EmployeeLeaveApplicationService {
	
	public LeaveApplicationStatusDto applyForLeave(Long employeeId,LeaveApplicationDto leaveApplicationDto);
	public LeaveApplicationStatusDto getLeaveApplicationById(Long employeeId,Long LeaveApplicationId);
	
	public long getUserIdByEmail(String loggedEmail);
}
