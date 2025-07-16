package itkhamar.com.model;

import itkhamar.com.enums.EmployeeStatus;
import itkhamar.com.enums.EmployeeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "employees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    private String id;

    private String name;
    private String phone;
    private String department;
    private String designation;
    private LocalDate joiningDate;
    private EmployeeStatus status;
    private EmployeeType type;

    private LocalDate createdAt;
    private LocalDate updatedAt;
}
