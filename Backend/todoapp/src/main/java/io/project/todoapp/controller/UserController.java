package io.project.todoapp.controller;

import io.project.todoapp.security.user.User;
import io.project.todoapp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class UserController {

    private static final String ADMIN_PREFIX = "/admin";

    private final UserService userService;

    @CrossOrigin(origins = "*")
    @GetMapping("/students/{id}")
    public User getStudent(@PathVariable Long id) {
        return userService.getStudentById(id);
    }

    @PostMapping(value = ADMIN_PREFIX + "/students")
    public void addNewStudent(@RequestBody User user) {
        userService.addNewStudent(user);
    }

    @DeleteMapping(ADMIN_PREFIX + "/students/{email}")
    public void deleteStudent(@PathVariable String email) {
        userService.deleteStudent(email);
    }

}
