package org.example;

import org.example.domain.Student;
import org.example.repository.StudentMysqlRepository;

public class Main {
    public static void main(String[] args) {

        StudentMysqlRepository studentMysqlRepository = new StudentMysqlRepository();
        System.out.println(studentMysqlRepository.findAll());

        System.out.println(studentMysqlRepository.findStudentById(1));

        Student student = Student.builder()
                .name("Sidorenku")
                .age(21)
                .groupId(1)
                .build();

        studentMysqlRepository.saveStudent(student);
        System.out.println(studentMysqlRepository.findAll());
    }
}