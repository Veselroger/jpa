package com.github.veselroger.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Setter @Getter
public class Professor {
    @Id
    @SequenceGenerator(name = "prof_seq", sequenceName = "professor_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prof_seq")
    private Long id;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "professor")
    private Set<Course> courses = new HashSet<Course>();
}
