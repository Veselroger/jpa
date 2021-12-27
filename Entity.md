# [←](./README.md) <a id="home"></a> Entity

## Table of Contents:
- [JPA Entity](#entity)
- [Equals and HashCode](#equalsHash)
- [Access Type](#access)
- [Unique identifier](#id)
- [Entity Lifecycle](#lifecycle)
--------

## [↑](#home) <a id="entity"></a> JPA Entity
JPA работает с Entity, поэтому очень важно понимать, что это такое.\
Лекция о Entity: **"[Thorben Janssen: Basic Entity Mappings](https://www.youtube.com/watch?v=QVpQodGBb8U)"**.

JPA Entity - это **POJO** (Plain Old Java Object), то есть это обычные Java объекты, без каких-либо специальных родительских классов или интерфейсов, только специальные аннотации.

JPA сущности формируют **Domain Model** приложения. Создадим их в отдельном пакете **model**.

Ранее мы создали БД скрипт (файл **create.sql**). Опишем в нём на языке SQL сущность **Professor**:
```sql
CREATE TABLE professor (
    id bigint NOT NULL,
    firstname character varying(255),
    lastname character varying(255)
);
ALTER TABLE professor ADD CONSTRAINT professor_pkey PRIMARY KEY (id);	
CREATE SEQUENCE professor_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
```

Первое требование - наш POJO должен быть **[Top Level Class](https://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html)** и класс не должен быть финальным (чтобы можно было сделать прокси):
```java
public class Professor {
    private Long id;
    private String firstName;
    private String lastName;
}
```

Чтобы данный **POJO** стал сущностью необходимо следующее:
- наличие аннотация **@Entity** над классом
- наличие конструктора БЕЗ аргументов
- наличие уникального идентификатора (аннотацией **@Id**)

```java
@Entity
@NoArgsConstructor
@Setter @Getter
public class Professor {
    @Id
    private Long id;

    private String firstName;
    private String lastName;
}
```

Подробнее про сущности см. лекцию **"[Thorben Janssen: Basic Entity Mappings](https://www.youtube.com/watch?v=QVpQodGBb8U)"**.


## [↑](#home) <a id="access"></a> Access Type
На основе того, где указана аннотация **ID** определяется **Access Type** для сущности.

**Access Type** определяет, каким образом реализация JPA получит доступ к данным из Java объекта.\
Существует два типа доступа: **FIELD** и **PROPERTY**.\
Подробнее можно прочитать в документации реализации JPA: **[Hibernate Access strategies](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#access)**.

По умолчанию, если **@Id** стоит над полем - то применяется Field Access, а если над методом - то Property Access. 

Access Type можно переопределить для конкретной сущности, если указать аннотацию **@Access**.\
На эту тему так же рекомендуется:
- **"[Overriding the default access strategy](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#_overriding_the_default_access_strategy)"**
- **"[Thorben Janssen: Access Strategies in JPA and Hibernate – Which is better, field or property access?](https://thorben-janssen.com/access-strategies-in-jpa-and-hibernate/)"**.

Согласно разделу **"2.3.2 Explicit Access Type"** спецификации JPA:
> This explicit access type specification does not affect the access type of other entity
classes or mapped superclasses in the entity hierarchy.

Если поле/метод нужно исключить поле из Entity, то необходимо использовать аннотацию **@Transient**.


## [↑](#home) <a id="id"></a> Unique identifier
Идентификатор для сущности при добавлении её в БД может быть сгенерировано автоматически при помощи реализации JPA.\
Для этого используется аннотация **@GeneratedValue**. Подробнее см. **[Thorben Janssen: Generate Identifiers Using JPA and Hibernate](https://thorben-janssen.com/jpa-generate-primary-keys)**.

Аннотация **@GeneratedValue** позволяет указать стратегию генерации ID (**GenerationType**).\
Наиболее часто используемые стратегии:
- **[GenerationType.IDENTITY](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#identifiers-generators-identity)** : возвращает ID, если используемая БД позволяет его получить как часть выполненного запроса.
- **[GenerationType.SEQUENCE](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#identifiers-generators-sequence)** : рекомендуемая стратегия, которая использует обращения к sequence на стороне БД.

По умолчанию, **GenerationType.SEQUENCE** использует sequence с именем "hibernate_sequence".\
При помощи **@SequenceGenerator** можно изменить используемую sequence:
```java
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
}
```
Подробнее см. статьи **[Thorben Janssen: Generate Identifiers Using JPA and Hibernate](https://thorben-janssen.com/jpa-generate-primary-keys)** и **"[Vlad Mihalcea: How to generate JPA entity identifier values using a database sequence](https://vladmihalcea.com/jpa-entity-identifier-sequence/)"**.


## [↑](#home) <a id="equalsHash"></a> Equals and HashCode
Важно понимать, как должны быть реализованы Equals и HashCode.\
Более подробно можно прочитать в материалах:
- **"[Ultimate Guide to Implementing equals() and hashCode() with Hibernate](https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/)"**
- **"[How to implement equals and hashCode using the JPA entity identifier](https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/)"**

Стоит придерживаться основного требования:
> Equals and hashCode must behave consistently across all entity state transitions.

Кроме того, говоря про Lombok стоит помнить про нюансы, описанные в материале **"[Lombok & Hibernate: How to Avoid Common Pitfalls](https://thorben-janssen.com/lombok-hibernate-how-to-avoid-common-pitfalls/)"**. Например, не стоит использовать аннотацию **@Data**.

--------

## [↑](#home) <a id="lifecycle"></a> Entity Lifecycle
У Entity есть свой жизненный цикл, за которым следит JPA Provider, например Hibernate.\
Подробнее можно ознакомиться к статье от Thorben Jannsen: **"[JPA & Hibernate: Entity Lifecycle Model](https://thorben-janssen.com/entity-lifecycle-model/)"**.

JPA вводит понятие **Persistence Context**. Согласно спецификации Persistence context - это некоторая общность сущностей, некоторый контейнер с сущностями. А за Persistence Context следит Entity Manager:
> Within the persistence context, the entity instances and their lifecycle
are managed by the entity manager.

**Transient** - это состояние, с которого начинается жизненный путь Entity.\
Пока Java объект не имеет на стороне источника данных (т.е. на стороне БД) соответствующей ему строчки - такой объект имеет статус **transient**, т.е. временный.

Чтобы из transient перевести объект в состояние **managed** (т.е. "управляемый" entity manager'ом) необходимо через Entity Manager'а выполнить действие **persist** (т.е. сохранить).

Сущность в состоянии **managed** может так же появиться при помощи метода find у Entity Manager'а. 

Дальше сущность может перейти в состояние **removed** или **detached**.

**Detached** - это состояние сущности, когда за ней больше не следит Entity Manager (т.е. сущности нет в Persistence Context), но при этом на стороне БД для сущности есть строка. В этом основное отличие detached от transient.\
Подробнее см. **"[How to remove entities from the persistence context](https://thorben-janssen.com/hibernate-tips-remove-entities-persistence-context/)"**.

По данной теме так же рекомендуется статья **"[What’s the difference between persist, save, merge and update?](https://thorben-janssen.com/persist-save-merge-saveorupdate-whats-difference-one-use/)"**.

Кроме того, Persistence Context также иногда называют **First Level Cache**:
- **[The JPA and Hibernate first-level cache](https://vladmihalcea.com/jpa-hibernate-first-level-cache/)**.
- **[How to use Hibernate's 1st level cache](https://www.youtube.com/watch?v=0lZnBTTbmRQ)**

Для начала, создадим абстрактны класс, в котором опишем обслуживание Entity Manager'а:
```java
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
```

Добавим тест на сохранение сущности Professor:
```java
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
```

--------

Перейти [к оглавлению](#home) или [другим темам](./README.md).