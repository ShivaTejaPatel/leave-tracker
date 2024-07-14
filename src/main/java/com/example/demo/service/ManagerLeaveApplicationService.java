package com.example.demo.service;

import java.util.List;

import com.example.demo.payload.LeaveApplicationStatusDto;
import com.example.demo.payload.LeaveStatusUpdate;

public interface ManagerLeaveApplicationService {
	
	public List<LeaveApplicationStatusDto> getAllLeaveApplications(Long managerId);
	
	public LeaveApplicationStatusDto getLeaveApplicationById(Long managerId,Long employeeId,Long LeaveApplicationId);
	
	public LeaveApplicationStatusDto updateLeaveApplication(Long managerId,Long employeeId,Long LeaveApplicationId,LeaveStatusUpdate leaveStatusUpdate);
	
	public long getUserIdByEmail(String loggedEmail);
}
