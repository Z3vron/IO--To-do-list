package io.project.todoapp.security.auth;

import io.project.todoapp.model.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterClassPresidentRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Semester semester;
}
