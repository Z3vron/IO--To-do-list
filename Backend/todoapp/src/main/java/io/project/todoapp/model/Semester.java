package io.project.todoapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "test")
public class Semester {

    @Id
    @GeneratedValue
    private Long id;
    private Integer number;
    private Integer year;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name="semester_subject", schema = "test")
    private List<Subject> subjects;
    private LocalDate startDate;
    private LocalDate endDate;
}
