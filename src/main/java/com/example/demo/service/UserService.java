package com.example.demo.service;

import com.example.demo.payload.UserDto;

public interface UserService {
	
	public  UserDto createEmployee(UserDto userDto);

	public boolean isUserWithEmailExists(String email);
}

