package itkhamar.com.dto;

import itkhamar.com.enums.EmployeeStatus;
import itkhamar.com.enums.EmployeeType;

import java.time.LocalDate;

public record EmployeeResponse(
        String name,
        String phone,
        String department,
        String designation,
        LocalDate joiningDate,
        EmployeeStatus status,
        EmployeeType type) {}
