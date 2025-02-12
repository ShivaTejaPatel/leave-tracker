package com.example.demo.entity;

import java.time.LocalDate;

import com.example.demo.payload.LeaveStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LeaveApplication {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    
    @Enumerated(EnumType.STRING)
    private LeaveStatus status;
    private String comment;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Users employee;
}

