package itkhamar.com.dto;

import java.util.List;

public record UserResponse(String id, String email, List<String> roles) {
}
