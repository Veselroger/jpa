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
-- One-to-One for Curriculum
ALTER TABLE curriculum
    ADD CONSTRAINT fk_curriculum_course FOREIGN KEY (id) REFERENCES course(id);
-- Many-to-One for Course
ALTER TABLE course
    ADD CONSTRAINT fk_course_professor FOREIGN KEY (professor_id) REFERENCES professor(id);
-- Many-to-Many for Course and Student
CREATE TABLE course_student (
    courses_id bigint NOT NULL,
    students_id bigint NOT NULL
);
ALTER TABLE course_student
    ADD CONSTRAINT fk_coursestudent_course FOREIGN KEY (courses_id) REFERENCES course(id);
ALTER TABLE course_student
    ADD CONSTRAINT fk_coursestudent_student FOREIGN KEY (students_id) REFERENCES student(id);