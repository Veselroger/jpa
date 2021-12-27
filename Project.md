# [←](./README.md) <a id="home"></a> JPA Project

## Table of Contents:
- [Maven Project](#maven)
- [JPA dependencies](#jpa)
- [Логирование](#log)
- [Lombok](#lombok)
- [persistence.xml](#persistence)
- [Schema generation](#schema)

--------

## [↑](#home) <a id="maven"></a> Maven Project
Для начала необходимо создать Java проект.\
Воспользуемся системой сборки проектов **[Maven](https://maven.apache.org/)**.

Maven описывает проекты при помощи **pom.xml** файлов, которые содержат **Project Object Model**.\
Можно создать **[минимальный pom.xml](https://maven.apache.org/guides/introduction/introduction-to-the-pom.html#Minimal_POM)** вручную. Например:
```xml
<project>
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.github.veselroger</groupId>
  <artifactId>jpa-workbook</artifactId>
  <version>1.0-SNAPSHOT</version>
</project>
```

Минимальный pom.xml должен указывать версию описания проекта (**modelVersion**), а так же **[Maven Coordinates](https://maven.apache.org/pom.html#Maven_Coordinates)** нашего проекта. В качестве версии укажем **"[SNAPSHOT версию](https://maven.apache.org/guides/getting-started/index.html#What_is_a_SNAPSHOT_version)"**.

Есть более простой способ - использовать **"[Maven Archetype](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html)"** и команду ``mvn archetype:generate``.

Если не указать архетип - будет использован архетип по умолчанию - **[maven-archetype-quickstart](https://maven.apache.org/archetypes/maven-archetype-quickstart/index.html)**.\
При создании из quickstart архетипа желательно удалить из pom.xml всё лишнее: тэги **name** и **url**, блок **build** (т.к. заполнять его будем позже и только нужными нам настройками), а так же блок **properties**.

Как система сборки, Maven вводит свой **"[Standard Directory Layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html)"**, согласно которому:
- Исходный код (каталог **src**) разделён на 2 категории: **main** (основное выполнение) и **test** (тесты).
- Каждая из категорий имеет каталог для java классов (**java**) и ресурсов (**resources**).

Очистим java файлы из **src/test/java** и **src/main/java**.\
Добавим каталоги **src/main/resources** и **src/test/resources**

В описании проекта (в файле pom.xml) укажем настройку используемой кодировки, о чём подробнее можно прочитать в документации Maven **"[Specifying a character encoding scheme](https://maven.apache.org/plugins/maven-resources-plugin/examples/encoding.html)"**:
```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

Архетип maven-archetype-quickstart по умолчанию используют JUnit 4.\
Изменим JUnit на версию 5 согласно руководству **"[Configuring Test Engines](https://junit.org/junit5/docs/current/user-guide/#running-tests-build-maven-engines-configure)"**:
```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <junit.version>5.8.2</junit.version>
</properties>

<dependencies>
    <dependency>
		<groupId>org.junit.jupiter</groupId>
		<artifactId>junit-jupiter</artifactId>
		<version>${junit.version}</version>
		<scope>test</scope>
	</dependency>
</dependencies>
```

Функциональность в Maven подключается и настраивается при помощи плагинов:
```xml
<build>
    <plugins>    
    </plugins>
</build>
```

Добавим настройки для **"[maven-compiler-plugin](https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-source-and-target.html)"**: 
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.7.0</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Кроме этого настроим плагин **[maven-surefire-plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)**, который отвечает за запуск тестов:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit.version}</version>
        </dependency>
    </dependencies>
</plugin>
```

--------

## [↑](#home) <a id="jpa"></a> JPA dependencies
Прежде всего, необходимо подключить Java Persistence API. Будем использовать версию JPA 2.2.\
Подробнее см. **"[Thorben Janssen: The difference between JPA and Hibernate](https://thorben-janssen.com/difference-jpa-hibernate-eclipselink/)"**.

Найдём **[Maven Coordinates](https://maven.apache.org/pom.html#Maven_Coordinates)** для **javax.persistence-api** на сайте **[Maven Repository](https://mvnrepository.com)**:
```xml
<dependency>
	<groupId>javax.persistence</groupId>
	<artifactId>javax.persistence-api</artifactId>
	<version>2.2</version>
</dependency>
```

Далее необходимо подключить реализацию JPA. Данный проект будет использовать **Hibernate**.\
Рекомендуется к прочтению: **"[Thorben Janssen: Getting Started With Hibernate](https://thorben-janssen.com/hibernate-getting-started)"**.

Подключим Hibernate согласно руководству **[Getting started with Hibernate ORM](https://hibernate.org/orm/documentation/getting-started/)**:
```xml
<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-core</artifactId>
	<version>5.6.2.Final</version>
</dependency>
```

Далее необходимо подключить источник данных (Data Source).\
В качестве баз данных подключим H2 Database согласно **"[H2 cheat sheet](https://www.h2database.com/html/cheatSheet.html)"**:
```xml
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
	<version>1.4.200</version>
</dependency>
```

--------

## [↑](#home) <a id="log"></a> Логирование
Согласно **[Hibernate Logging Guide](https://docs.jboss.org/hibernate/orm/5.6/topical/html_single/logging/Logging.html)** мы можем подключить разные фрэймворки логирования.

Подключим к проекту Log4J v2 согласно документации **"[Using Log4j in your Apache Maven build](https://logging.apache.org/log4j/2.x/maven-artifacts.html)"**.\
Добавим настройку "log4j.version" для версии log4j в блок properties:
```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <junit.version>5.8.2</junit.version>
    <log4j.version>2.15.0</log4j.version>
</properties>
```

Далее подключим сами зависимости:
```xml
<dependency>
	<groupId>org.apache.logging.log4j</groupId>
	<artifactId>log4j-api</artifactId>
	<version>${log4j.version}</version>
</dependency>
<dependency>
	<groupId>org.apache.logging.log4j</groupId>
	<artifactId>log4j-core</artifactId>
	<version>${log4j.version}</version>
</dependency>
```

Настроим log4j согласно **"[Thorben Janssen: Hibernate Logging Guide](https://thorben-janssen.com/hibernate-logging-guide/)"**.\
Добавим файл **log4j2.xml** в каталог **src/main/resources**:
<details>
  <summary>Пример log4j2.xml</summary>
  
```xml
<Configuration monitorInterval="60">
    <Properties>
        <Property name="log-path">PropertiesConfiguration</Property>
    </Properties>
    <Appenders>
        <Console name="Console-Appender" target="SYSTEM_OUT">
            <PatternLayout>
                <pattern>
                    [%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n
                </pattern>>
            </PatternLayout>
        </Console>
    </Appenders>
    <Loggers>
        <Logger name="org.hibernate.SQL" level="debug" additivity="false">
            <AppenderRef ref="Console-Appender"/>
        </Logger>
        <Logger name="org.hibernate.type.descriptor.sql" level="trace" additivity="false">
            <AppenderRef ref="Console-Appender"/>
        </Logger>
        <Root level="info">
            <AppenderRef ref="Console-Appender"/>
        </Root>
    </Loggers>
</Configuration>
```
</details>

--------

## [↑](#home) <a id="lombok"></a> Lombok
Чтобы уменьшить количество шаблонного кода (boilerplate кода) подключим к проекту **[Lombok](https://projectlombok.org/setup/maven)**.

Укажем версию lombok в блоке properties:
```xml
<lombok.version>1.18.22</lombok.version>
```

Подключим саму зависимость для Lombok:
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>${lombok.version}</version>
    <scope>provided</scope>
</dependency>
```

Использование Lombok требует настройки процессоров аннотаций.\
Добавим в настройки Maven Compiler Plugin (блок **configuration**) настройки процессора аннотаций согласно документации **[annotation Processor Path](https://projectlombok.org/setup/maven)**:
```xml
<configuration>
    <annotationProcessorPaths>
        <path>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </path>
    </annotationProcessorPaths>
```

Про Hibernate и Lombok есть полезные материалы:
- [Thorben Janssen: Lombok & Hibernate: How to Avoid Common Pitfalls](https://thorben-janssen.com/lombok-hibernate-how-to-avoid-common-pitfalls/)
- [Lombok + JPA: Что может пойти не так?](https://habr.com/ru/company/haulmont/blog/564682/)

--------

## [↑](#home) <a id="persistence"></a> persistence.xml
Для настройки JPA используется специальный файл - **persistence.xml**.\
Подробнее можно прочитать в статье: **[Thorben Janssen: A Beginner’s Guide to JPA’s persistence.xml](https://thorben-janssen.com/jpa-persistence-xml/)**.

Про **persistence.xml** можно прочитать в спецификации JPA, например в **"[JSR 338: JavaTM Persistence API 2.2](https://download.oracle.com/otn-pub/jcp/persistence-2_2-mrel-spec/JavaPersistence.pdf)"**.

Согласно главе **"8.2.1 persistence.xml file"** файл persistence.xml должен лежать в каталоге **META-INF**:
> The persistence.xml file is located in the META-INF directory

В **Maven Project Layout** есть 2 каталога с ресурсами: src/main/resources и src/test/resources.
Файлы ресурсов благодаря [Maven Resources Plugin'у](https://maven.apache.org/plugins/maven-resources-plugin/) окажутся на classpath, а следовательно подкаталог META-INF следует создать именно в них.

Т.к. мы будем рассматривать JPA при помощи тестов, то создадим каталог **src/test/resources/META-INF**.

В спецификации JPA в главе **8.3 persistence.xml Schema** приведён пример **persistence.xml**:
```xml
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
 http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd"
 version="2.2">

</persistence>
```

Файл **persistence.xml** состоит из Persistence Unit'ов.\
**Persistence Unit** - это логическая единица, в рамках которой настраивается работа с JPA.

Добавим описание **Persistence Unit** в **persistence.xml**:
```xml
<persistence-unit name="SimpleUnit">
	<description>Simple Persistence Unit</description>
	<provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
</persistence-unit>
```
Тэг **provider** указывает не реализацию JPA. Подробнее см. **"[hibernate User Guide: JPA Bootstrapping](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#bootstrap-jpa)"**.

Каждый persistence-unit имеет блок настроек.\
Например, настроим **[Hibernate Dialect](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#database-dialect)** при помощи настройки **hibernate.dialect**:
```xml
<properties>
    <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
</properties>
```

Кроме этого, нужно указать настройки подключения к источнику данных:
```xml
<property name="javax.persistence.jdbc.driver" value="org.h2.Driver" />
<property name="javax.persistence.jdbc.user" value="sa" />
<property name="javax.persistence.jdbc.password" value="" />
<property name="javax.persistence.jdbc.url"
            value="jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1" />
```
Пример данных настроек можно подсмотреть в **"[Hibernate User Guide: JPA Bootstrapping](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#bootstrap-jpa)"**.

На основе материала **"[Thorben Janssen: JPA’s Bootstrapping API](https://thorben-janssen.com/hibernate-getting-started/#JPA8217s_Bootstrapping_API)"** напишем небольшой тест в **src/test/java**:
```java
public class PersistenceTest {
    @Test
    public void persistenceTest() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SimpleUnit");
        EntityManager em = emf.createEntityManager();
        Assertions.assertTrue(em.isOpen());
        em.getTransaction().begin();
        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}
```
Наш JPA проект готов к работе.

--------

## [↑](#home) <a id="schema"></a> Schema generation
Прежде чем начать, необходимо подготовить базу данных для наших тестов. 

Подробнее про создание схемы БД см. **[Hibernate User Guide: Schema generation](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#schema-generation)"**. 

В документации сказано, что за генерацию схемы отвечает механизм **HBM2DDL** (HiBernate Mapping To DDL):
> the process of generating schema from entity mapping has been called HBM2DDL

Воспользуемся статьёй **"[Thorben Janssen: Standardized schema generation and data loading with JPA 2.1](https://thorben-janssen.com/standardized-schema-generation-data-loading-jpa-2-1/)"** и добавим настройки:
```xml
<property name="javax.persistence.schema-generation.database.action" value="create"/>
<property name="javax.persistence.schema-generation.create-source" value="script"/>
<property name="javax.persistence.schema-generation.create-script-source" value="./create.sql"/>
```

Кроме этого, нужно настроить **hibernate.hbm2ddl.import_files_sql_extractor** на чтение многострочных SQL из файла:
```xml
<property name="hibernate.hbm2ddl.import_files_sql_extractor" value="org.hibernate.tool.hbm2ddl.MultipleLinesSqlCommandExtractor" />
```

Теперь остаётся в каталоге ресурсов создать файл **create.sql** и заполнять его.\
За основу возьмём курс **"[Thorben Janssen: JPA for Beginners 2.0](https://thorben-janssen.com/courses/jpa-for-beginners-2-0/)"**.\
Будем заполнять файл по примеру из **[jpaForBeginners-Example.txt](https://thorben-janssen.com/wp-content/uploads/member-access/courses/jpaBeginners/slides/jpaForBeginners-Example.txt)**.

--------

Перейти [к оглавлению](#home) или [другим темам](./README.md).