package com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.jmachuca.curso.springboot.jpa.springboot_jpa_relationship.entities.Course;

public interface CourseRepository extends CrudRepository<Course, Long> {

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.id = ?1")
    Optional<Course> findOneWithStudents( Long id);
}
