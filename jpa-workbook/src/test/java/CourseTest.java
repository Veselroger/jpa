import com.github.veselroger.model.Course;
import com.github.veselroger.model.Student;
import com.github.veselroger.service.CourseService;
import com.github.veselroger.service.StudentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

    @Test
    public void shouldEnrollStudentToCourse() {
        em.getTransaction().begin();
        CourseService courseService = new CourseService(em);
        StudentService studentService = new StudentService(em);
        Course course = new Course();
        course.setName("test");
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        studentService.saveStudent(student); // Make student managed
        course.addStudent(student);
        courseService.saveCourse(course);
        Assertions.assertNotNull(courseService.findByName("test"));
    }

}
