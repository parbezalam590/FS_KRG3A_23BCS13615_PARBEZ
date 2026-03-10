package com.example.demo.service;
import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.Student;
import java.util.List;

public interface StudentService {
    Student createStudent(StudentDTO studentDTO);//pass dto here not the actual object.
    List<Student> getAllStudents();
}