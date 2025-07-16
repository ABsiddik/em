package itkhamar.com.model;

import itkhamar.com.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String email;
    private String password;

    private Set<RoleEnum> roles;

    private String employeeId;

    private LocalDate createdAt;
    private LocalDate updatedAt;
}
