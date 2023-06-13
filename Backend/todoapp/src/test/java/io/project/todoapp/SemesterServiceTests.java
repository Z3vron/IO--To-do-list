package io.project.todoapp;

import io.project.todoapp.model.Semester;
import io.project.todoapp.model.Subject;
import io.project.todoapp.repository.SemesterRepository;
import io.project.todoapp.service.SemesterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SemesterServiceTests
{
    @Mock
    private SemesterRepository semesterRepository;

    @InjectMocks
    private SemesterService semesterService;

    @Test
    public void testGetAllSemesters()
    {
        Semester semester1 = new Semester(0L, 1, 2023, new ArrayList<Subject>(), LocalDate.parse("2023-01-01"), LocalDate.parse("2023-06-30"));
        Semester semester2 = new Semester(1L, 2, 2023, new ArrayList<Subject>(), LocalDate.parse("2023-07-01"), LocalDate.parse("2023-12-31"));
        Semester semester3 = new Semester(2L, 3, 2023, new ArrayList<Subject>(), LocalDate.parse("2024-01-01"), LocalDate.parse("2024-06-30"));

        ArrayList<Semester> semestersExpected = new ArrayList<>();
        Collections.addAll(semestersExpected, semester1, semester2, semester3);

        when(semesterRepository.findAll()).thenReturn(semestersExpected);

        List<Semester> SemestersActual = semesterService.getAllSemesters();

        assertEquals(semestersExpected, SemestersActual);
    }

    @Test
    public void testGetSemesterById()
    {
        Semester semesterExpected = new Semester(0L, 1, 2023, new ArrayList<Subject>(), LocalDate.parse("2023-01-01"), LocalDate.parse("2023-06-30"));

        when(semesterRepository.getReferenceById(0L)).thenReturn(semesterExpected);
        when(semesterRepository.getReferenceById(1L)).thenReturn(null);

        Semester semesterActual = semesterService.getSemesterById(0L);

        assertEquals(semesterExpected, semesterActual);

        semesterActual = semesterService.getSemesterById(1L);

        assertEquals(null, semesterActual);
    }
}
