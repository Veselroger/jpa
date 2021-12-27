package com.github.veselroger.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Setter @Getter
@NamedQuery(name = Student.QUERY_STUDENT_BY_FIRSTNAME_AND_LASTNAME,
        query = "SELECT s FROM Student s WHERE s.firstName = :"+Student.PARAM_FIRSTNAME+" AND s.lastName = :"+Student.PARAM_LASTNAME)
public class Student {
    public static final String QUERY_STUDENT_BY_FIRSTNAME_AND_LASTNAME = "query.StudentByFirstNameAndLastName";
    public static final String PARAM_FIRSTNAME = "firstName";
    public static final String PARAM_LASTNAME = "lastName";

    @Id
    @SequenceGenerator(name = "student_seq", sequenceName = "student_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private StudentState state;

    private String firstName;
    private String lastName;

    @ManyToMany(mappedBy = "students")
    private Set<Course> courses = new HashSet<Course>();

}
