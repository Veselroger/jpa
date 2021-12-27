package com.github.veselroger.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Setter @Getter
public class Course {
    @Id
    @SequenceGenerator(name = "course_seq", sequenceName = "course_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_seq")
    private Long id;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToOne(mappedBy = "course")
    private Curriculum curriculum;

    @ManyToOne(fetch = FetchType.LAZY)
    private Professor professor;

    @ManyToMany
    @JoinTable(name = "course_student",
            joinColumns = { @JoinColumn(name = "courses_id") },
            inverseJoinColumns = { @JoinColumn(name = "students_id") })
    private Set<Student> students = new HashSet<Student>();


    public void addStudent(Student student) {
        this.students.add(student);
        student.getCourses().add(this);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getCourses().remove(this);
    }
}
