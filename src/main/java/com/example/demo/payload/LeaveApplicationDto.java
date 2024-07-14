package com.example.demo.payload;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplicationDto {
	private Long id;
	private LocalDate startDate;
	private LocalDate endDate;
	private String reason;
}
