import com.github.veselroger.model.Professor;
import org.junit.jupiter.api.Test;

public class ProfessorTest extends PersistenceTest {

    @Test
    public void shouldPersistProfessor() {
        em.getTransaction().begin();
        Professor p = new Professor();
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }
}
