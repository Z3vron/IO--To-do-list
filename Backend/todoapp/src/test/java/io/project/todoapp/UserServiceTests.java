package io.project.todoapp;

import io.project.todoapp.model.Semester;
import io.project.todoapp.model.Subject;
import io.project.todoapp.security.user.Role;
import io.project.todoapp.security.user.User;
import io.project.todoapp.security.user.UserRepository;
import io.project.todoapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetStudentById()
    {
        Semester semester = new Semester(0L, 1, 2023, new ArrayList<Subject>(), LocalDate.parse("2023-01-01"), LocalDate.parse("2023-06-30"));

        User user = new User(0L, "Jan", "Nowak", "jan.nowak@student.agh.edu.pl", "Qwerty123", semester, Role.STUDENT);

        when(userRepository.findById(0L)).thenReturn(Optional.of(user));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertEquals(user, userService.getStudentById(0L));
        assertThrows(EntityNotFoundException.class, () ->
        {
            userService.getStudentById(1L);
        });
    }
}