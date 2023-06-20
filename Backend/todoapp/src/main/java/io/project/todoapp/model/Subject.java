package io.project.todoapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.Collection;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "test")
public class Subject {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer ectsPoints;

    @OneToMany(cascade = CascadeType.ALL)
    @Transient
    private List<Task> tasks = Collections.emptyList();
//    private boolean active = true;

}
