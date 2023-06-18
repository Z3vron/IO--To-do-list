package io.project.todoapp.security.auth;

import io.project.todoapp.model.Semester;
import io.project.todoapp.model.Subject;
import io.project.todoapp.model.Task;
import io.project.todoapp.security.config.JwtService;
import io.project.todoapp.security.user.Role;
import io.project.todoapp.security.user.User;
import io.project.todoapp.security.user.UserRepository;
import io.project.todoapp.service.SemesterService;
import io.project.todoapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SemesterService semesterService;
    private final TaskService taskService;

    public AuthenticationResponse register(RegisterRequest request) {

        Semester foundedSemester = semesterService.getSemesterById(request.getSemesterId());
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .actualSemester(foundedSemester)
                .build();
        
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }

    public void registerClassPresident(RegisterClassPresidentRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .actualSemester(request.getSemester())
                .build();

        userRepository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        List<Task> allTasksByUserId = taskService.findAll().stream().filter(e -> e.getUserId().equals(user.getId())).collect(Collectors.toList());
        List<Subject> subjects = user.getActualSemester().getSubjects();

            for (Subject subject : subjects) {
            List<Task> taskBySubject = allTasksByUserId.stream().filter(e -> e.getSubjectId().equals(subject.getId())).collect(Collectors.toList());
            subject.setTasks(taskBySubject);
        }

        String jwtToken = jwtService.generateToken(user);
//        String jwtToken = "";
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(user)
                .build();
    }
}
