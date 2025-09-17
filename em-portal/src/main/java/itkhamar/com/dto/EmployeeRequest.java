package itkhamar.com.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EmployeeRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
        String phone,

        @NotBlank(message = "Department is required")
        String department,

        @NotBlank(message = "Designation is required")
        String designation,

        @NotNull(message = "Joining date is required")
        @PastOrPresent(message = "Joining date cannot be in the future")
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate joiningDate
) { }
