package com.example.demo.service.impl;
//implemntaion is differenty from interface, we have to implement all the methods of interface in implementation class, and we can also add some additional methods in implementation class if needed, but we have to provide implementation for all the methods of interface in implementation class, otherwise we will get error.
////it seprates type of students from service logic, so that we can easily manage and maintain the code, and also it helps in achieving loose coupling between different layers of the application, which is a good practice in software development.
import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.repository.StudentRepository;

@Service
public class StudentServiceimpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceimpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    @Override
    public Student createStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setCourse(studentDTO.getCourse());
        return studentRepository.save(student);
    }
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
