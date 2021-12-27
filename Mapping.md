# [←](./README.md) <a id="home"></a> Simple Mapping

## Table of Contents:
- [Database schema](#schema)
- [Custom Mapping](#custom)
- [@Enumerated](#enumerated)
- [@Temporal](#temporal)
-**Associations**
    - [@OneToOne](#oneToOne)
    - [@ManyToOne](#manyToOne)
    - [@ManyToMany](#manyToMany) 
--------

## [↑](#home) <a id="schema"></a> Database schema
Для рассмотрения данной темы будет использован пример **[jpaForBeginners-Example.txt](https://thorben-janssen.com/wp-content/uploads/member-access/courses/jpaBeginners/slides/jpaForBeginners-Example.txt)** из курса **"[Thorben Janssen: JPA for Beginners 2.0](https://thorben-janssen.com/courses/jpa-for-beginners-2-0/)"**.

В каталоге ресурсов для тестов ранее мы создали БД скрипт (файл **create.sql**). Заполним его.
<details>
  <summary>Заполнение скрипта create.sql</summary>
  
```sql
-- Professor Entity
CREATE TABLE professor (
    id bigint NOT NULL,
    firstname character varying(255),
    lastname character varying(255)
);
ALTER TABLE professor ADD CONSTRAINT professor_pkey PRIMARY KEY (id);
CREATE SEQUENCE professor_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

-- Student Entity
CREATE TABLE student (
    id bigint NOT NULL,
    firstname character varying(255),
    lastname character varying(255),
    state integer
);
ALTER TABLE student ADD CONSTRAINT student_pkey PRIMARY KEY (id);
CREATE SEQUENCE student_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

-- Course Entity
CREATE TABLE course (
    id bigint NOT NULL,
    name character varying(255),
    startdate date,
	enddate date,
    professor_id bigint
);
ALTER TABLE course ADD CONSTRAINT course_pkey PRIMARY KEY (id);
CREATE SEQUENCE course_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

-- Curriculum Entity
CREATE TABLE curriculum (
    id bigint NOT NULL,
    description character varying(255)
);
ALTER TABLE curriculum ADD CONSTRAINT curriculum_pkey PRIMARY KEY (id);
```
</details>

Данный SQL скрипт можно визуализировать при помощи сервиса **[dbdiagram.io](https://dbdiagram.io/home)**.

--------

## [↑](#home) <a id="custom"></a> Custom Mapping
JPA позволяет настраивать отображение сущностей на источник данных.\
Подробнее см. **"[Thorben Janssen: Key annotations you need to know when working with JPA and Hibernate](https://thorben-janssen.com/key-jpa-hibernate-annotations/)"**.

Пример для сущности **"Professor"**:
```java
@Entity
@NoArgsConstructor
@Setter @Getter
@Table(name = "Professor")
public class Professor {
    @Id
    @SequenceGenerator(name = "prof_seq", sequenceName = "professor_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prof_seq")
    private Long id;
    
    @Column(name = "firstName")
    private String firstName;
    
    @Column(name = "lastName")
    private String lastName;
}
```

В дополнение рекомендуется к прочтению: **"[What’s the difference between @Column(nullable = false) and @NotNull](https://thorben-janssen.com/hibernate-tips:-whats-the-difference-between-column-nullable-false-and-notnull)"**.


## [↑](#home) <a id="enumerated"></a> @Enumerated
Одним из типов, отображение которых в БД может быть настроено, является **Enum**.\
Подробнее см. статью **"[Thorben Janssen: Enum Mappings with Hibernate](https://thorben-janssen.com/hibernate-enum-mappings)"**.

Представим следующий enum:
```java
public enum StudentState {
	ENROLLED, EXMATRICULATED;
}
```

При помощи аннотации **@Enumerated** можно сообщить, как нужно соотнести enum и колонку в БД.\
По умолчанию, Hibernate использует **EnumType.ORDINAL** для отображение enum на число, которое соответствует zero-based положению значения в enum'е.

Добавим **enum** в сущность **Student**:
```java
@Entity
@NoArgsConstructor
@Setter @Getter
public class Student {
    @Id
    @SequenceGenerator(name = "student_seq", sequenceName = "student_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private StudentState state;
    
    private String firstName;
    private String lastName;
}
```

Иногда отображение enum'ов должны быть более "хитрые". В этом случае помогают конвертеры.\
Подробнее см. материал **"[Thorben Janssen: Attribute Converter – The better way to persist enums](https://thorben-janssen.com/jpa-21-type-converter-better-way-to)"**.

Кроме того, по теме enum'ов следует ознакомиться с материалом: **"[The best way to map an Enum Type with JPA and Hibernate](https://vladmihalcea.com/the-best-way-to-map-an-enum-type-with-jpa-and-hibernate/)"**.


## [↑](#home) <a id="temporal"></a> @Temporal
JPA позволяет отображать в том числе дату и время.\
Подробнее можно прочитать в статье: **"[Date and Time Mappings with Hibernate and JPA](https://thorben-janssen.com/hibernate-jpa-date-and-time)"**.

Кроме того, начиная с JPA 2.2 добавлена поддержка **Date And Time API**: **"[How To Map The Date And Time API with JPA 2.2](https://thorben-janssen.com/map-date-time-api-jpa-2-2/)"**.

В случае, если на стороне Java дата и время представлены **[java.util.Date](https://www.tutorialspoint.com/java/java_date_time.htm)**:
```java
@Temporal(TemporalType.DATE)
private Date startDate;

@Temporal(TemporalType.DATE)
private Date endDate;
```
Тот же результат можно было бы получить используя LocalDate/LocalTime в качестве типов полей.

Пример для сущности **Course**:
```java
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
}
```

Кроме того, про даты рекомендуется к прочтению материал: **[Java и время: часть первая](https://habr.com/ru/post/274811/)**.


## [↑](#home) <a id="oneToOne"></a> @OneToOne
Одна из простейших ассоциаций между сущностями - **One To One** (один к одному).

Добавим в наш **create.sql** скрипт создания **Foreign Key** для таблицы **curriculum**:
```sql
-- One-to-One for Curriculum
ALTER TABLE curriculum
    ADD CONSTRAINT fk_curriculum_course FOREIGN KEY (id) REFERENCES course(id);
```
Стоит заострить своё внимание на том, что Foreign Key проверяется на вставке, а следовательно на стороне БД обычно связь есть только с одной стороны. То есть со стороны БД обычно связь односторонняя.\
И получается, что curriculum является владельцем связи этих двух таблиц.

Теперь опишем сущность **Curriculum**:
```java
@Entity
@NoArgsConstructor
@Setter @Getter
public class Curriculum {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Course course;
    
    private String description;
}
```
Т.к. FK на стороне Curriculum, то мы используем аннотацию **@JoinColumn** с указанием названия колонки.

На стороне сущности **Course** мы можем так же указать ассоциацию:
```java
@OneToOne(mappedBy = "course")
private Curriculum curriculum;
```
Т.к. таблица course не содержит колонки для связи с curriculum, поэтому мы используем mappedBy, чтобы указать JPA как ему искать связь с курсами.

Подробнее про One To One ассоциации:
- [Map OneToOne Associations with JPA and Hibernate](https://thorben-janssen.com/ultimate-guide-association-mappings-jpa-hibernate/#oneToOne)
- [How to Share the Primary Key in a One-to-One Association](https://thorben-janssen.com/hibernate-tips-same-primary-key-one-to-one-association/)


## [↑](#home) <a id="manyToOne"></a> @ManyToOne
Продолжим заполнение сущности Course и опишем ассоциацию c сущностью **Professor**.\
Данная ассоциация имеет тип **ManyToOne**, т.е. каждый One профессор ведёт Many курсов.

В нашем БД скрипте добавим строчку:
```sql
-- Many-to-One for Course
ALTER TABLE course
    ADD CONSTRAINT fk_course_professor FOREIGN KEY (professor_id) REFERENCES professor(id);
```

Теперь на стороне сущности **Course** мы можем указать следующее:
```java
@ManyToOne(fetch = FetchType.LAZY)
private Professor professor;
```

На стороне сущности **Professor** укажем коллекцию курсов:
```java
@OneToMany(mappedBy = "professor")
private Set<Course> courses = new HashSet<Course>();
```

Обычно, one-to-many является referencing стороной в bidirectional связи. Однако, иногда можно встретить его как самостоятельную unidirectional связь. В этом случае необходимо указать аннотацией JoinColumn где находится FK столбец, иначе Hibernate будет использовать отдельную таблицу для связи сущностей.

Подробнее про Many-To-One ассоциации:
- [Map ManyToOne Associations with JPA and Hibernate](https://thorben-janssen.com/ultimate-guide-association-mappings-jpa-hibernate#manyToOne)


## [↑](#home) <a id="manyToMany"></a> @ManyToMany
Осталось рассмотреть ассоциацию "Многие ко многим", она же **Many-to-Many**.\
Подробнее написано в статье **"[Map Many-to-Many Associations with JPA and Hibernate](https://thorben-janssen.com/ultimate-guide-association-mappings-jpa-hibernate/#manyToMany)"**.

Снова воспользуемся примером **[jpaForBeginners-Example.txt](https://thorben-janssen.com/wp-content/uploads/member-access/courses/jpaBeginners/slides/jpaForBeginners-Example.txt)** и добавим запросы в наш БД скрипт:
```sql
-- Many-to-Many for Course and Student
CREATE TABLE course_student (
    courses_id bigint NOT NULL,
    students_id bigint NOT NULL
);
ALTER TABLE course_student
    ADD CONSTRAINT fk_coursestudent_course FOREIGN KEY (courses_id) REFERENCES course(id);
ALTER TABLE course_student
    ADD CONSTRAINT fk_coursestudent_student FOREIGN KEY (students_id) REFERENCES student(id);
```

По умолчанию, Hibernate ищет таблицу как ``<owning_table>_<referencing_table>``.\
Выберем в качестве owning стороны сущность **Course**:
```java
@ManyToMany
private Set<Student> students = new HashSet<Student>();
```

А в качестве referencing стороны сущность **Student**:
```java
@ManyToMany(mappedBy = "students")
private Set<Course> courses = new HashSet<Course>();
```

Тогда Hibernate будет искать в нужной нам таблице **course_student**, что будет равносильно следующим аннотациям:
```java 
@ManyToMany
@JoinTable(name = "course_student", 
        joinColumns = { @JoinColumn(name = "courses_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "students_id") })
private Set<Student> students = new HashSet<Student>();
```

Кроме того, стоит помнить, что Hibernate не занимается сам обслуживанием коллекций. Поэтому считается хорошей практикой использовать методы, через которые будут добавляться и удаляться many-to-many элементы:
```java
public void addStudent(Student student) {
    this.students.add(student);
    student.getCourses().add(this);
}

public void removeStudent(Student student) {
    this.students.remove(student);
    student.getCourses().remove(this);
}
```

По данной теме рекомендуются материалы:
- [Best Practices for Many-to-Many Associations with Hibernate and JPA](https://thorben-janssen.com/best-practices-for-many-to-many-associations-with-hibernate-and-jpa/)
- [Hibernate Tip: Many-to-Many Association with additional Attributes](https://thorben-janssen.com/hibernate-tip-many-to-many-association-with-additional-attributes/)

--------

Перейти [к оглавлению](#home) или [другим темам](./README.md).