import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class PersistenceTest {
    protected static EntityManagerFactory emf;
    protected EntityManager em;

    @BeforeAll
    public static void init() {
        emf = Persistence.createEntityManagerFactory("SimpleUnit");
    }

    @AfterAll
    public static void close() {
        emf.close();
    }

    @BeforeEach
    public void initEm() {
        em = emf.createEntityManager();
    }

    @AfterEach
    public void closeEm() {
        if (em.isOpen()) {
            em.close();
        }
    }
}
