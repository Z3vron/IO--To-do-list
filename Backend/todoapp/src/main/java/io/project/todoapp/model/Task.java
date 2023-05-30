package io.project.todoapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "test")
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private Long subjectId;
    private String name;
    private String description;
    private Boolean done;
}