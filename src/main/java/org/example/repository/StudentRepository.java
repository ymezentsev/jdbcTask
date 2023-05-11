package org.example.repository;

import org.example.domain.Student;

import java.util.List;

public interface StudentRepository {
    List<Student> findAll();

    Student findStudentById(int id);

    void saveStudent(Student student);

}
