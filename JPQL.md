# [←](./README.md) <a id="home"></a> Java Persistence Query Language

## Table of Contents:
- [Java Persistence Query Language](#jpql)
- [Named Query](#named)
--------

## [↑](#home) <a id="jpql"></a> Java Persistence Query Language
JPQL - это специальный язык запросов, похожий на SQL, но работающий с доменной моделью, проще говоря с Entity.\
Подробнее про JPQL см. **"[Thorben Janssen: How to Define JPQL Queries in JPA and Hibernate](https://thorben-janssen.com/jpql/)"**

JPQL запросы могут быть созданы непосредственно в методе. Часто это можно встретить в сервисах, через которое происходит взаимодействие с БД.
Например, представим сервис для работы с курсами:
```java
public class CourseService {
	private EntityManager em;
	
	public CourseService(EntityManager em) {
		this.em = em;
	}
	
	public Course saveCourse(Course c) {
		em.persist(c);
		return c;
	}
	
	public Course findById(Long id) {
		return em.find(Course.class, id);
	}
	
	public void removeCourse(Course c) {
		em.remove(c);
	}
}
```

Тогда, мы можем добавить в серси использование JPQL:
```java
public Course findByName(String name) {
	TypedQuery<Course> q = em.createQuery("SELECT c FROM Course c WHERE c.name = :name", Course.class);
	q.setParameter("name", name);
	return q.getSingleResult();
}
	
public int countStudentsInCourse(Long id) {
	Query q = em.createQuery("SELECT SIZE(c.students) FROM Course c WHERE c.id = :id");
	q.setParameter("id", id);
	return (Integer) q.getSingleResult();
}
```

И теперь можем протестировать, что JPQL отрабатывает:
```java
public class CourseTest extends PersistenceTest {
    @Test
    public void shouldPersistCourse() {
        em.getTransaction().begin();
        CourseService svc = new CourseService(em);
        Course course = new Course();
        course.setName("test");
        svc.saveCourse(course);
        Assertions.assertNotNull(svc.findByName("test"));
    }
}
```

## [↑](#home) <a id="named"></a> Named Query
JPQL запросы могут быть сохранены под определённым именем для более удобного использования.
Например, для сущности студента можно добавить следующий код:
```java
@Entity
@NamedQuery(name = Student.QUERY_STUDENT_BY_FIRSTNAME_AND_LASTNAME, 
			query = "SELECT s FROM Student s WHERE s.firstName = :"+Student.PARAM_FIRSTNAME+" AND s.lastName = :"+Student.PARAM_LASTNAME)
public class Student {
	public static final String QUERY_STUDENT_BY_FIRSTNAME_AND_LASTNAME = "query.StudentByFirstNameAndLastName";
	public static final String PARAM_FIRSTNAME = "firstName";
	public static final String PARAM_LASTNAME = "lastName";
```

Далее, у нас есть сервис для работы со студентами:
```java
public class StudentService {

	private EntityManager em;
	
	public StudentService(EntityManager em) {
		this.em = em;
	}
	
	public Student saveStudent(Student s) {
		em.persist(s);
		return s;
	}
	
	public Student findById(Long id) {
		return em.find(Student.class, id);
	}
	
	public void removeStudent(Student s) {
		em.remove(s);
    }
}
```

В него мы теперь можем добавить метод, который использует Named Query:
```java
public List<Student> findByFirstNameAndLastName(String firstName, String lastName) {
	TypedQuery<Student> q = em.createNamedQuery(Student.QUERY_STUDENT_BY_FIRSTNAME_AND_LASTNAME, Student.class);
	q.setParameter(Student.PARAM_FIRSTNAME, firstName);
	q.setParameter(Student.PARAM_LASTNAME, lastName);
	return q.getResultList();
}
```

--------

Перейти [к оглавлению](#home) или [другим темам](./README.md).