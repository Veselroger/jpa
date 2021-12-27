package com.github.veselroger.service;

import com.github.veselroger.model.Course;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
}
