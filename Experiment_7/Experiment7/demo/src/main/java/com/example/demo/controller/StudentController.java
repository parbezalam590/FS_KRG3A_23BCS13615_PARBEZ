// package com.example.demo.controller;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import java.util.List;
// import jakarta.validation.Valid;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.http.*;
// @RestController
// @RequestMapping("/api/students")//anything passed in request mapping is  base url first page
// public class StudentController {
   
// }

package com.example.demo.controller;

import com.example.demo.dto.StudentDTO;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController
//it  tells here the object mapping is done 
@RequestMapping("/students")
public class StudentController {
//final keyword means this is construceter injection
//field injection mai ek baar create hue objet ko change kar skte hai par constructor injection mai ek baar create hue object ko change nahi kar skte hai, issi
//isiliye constructor injection mai final keyword use karte hai taki ek baar create hue object ko change na kiya ja sake

//+++++++++++++++++++++//extra//++++++++++++
//ya too declaration k time pe final hp skta hai ya fir construction k time per
// coupling is of two types
// singly coupling: ek class dusri class ke sath interact karti hai  linearly and second is doubly coupling: ek class dusri class ke sath interact karti hai and second class first class ke sath interact karti hai
private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
        
        @PostMapping
        public ResponseEntity<Student> createStudent(
                @Valid @RequestBody StudentDTO dto) {
            Student savedStudent = studentService.createStudent(dto);
            return ResponseEntity.ok().body(savedStudent);
        }

        @GetMapping
        public List<Student> getAllStudents() {
            return studentService.getAllStudents();
        }
        
    }
