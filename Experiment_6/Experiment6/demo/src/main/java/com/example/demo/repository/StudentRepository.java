package com.example.demo.repository;
import com.example.demo.entity.Student;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
//spring automatically generates sql queries for us, so we dont have to write any sql queries, we can just use the methods provided by jpa repository, such as save(), findById(), findAll(), deleteById() etc. and we can also define custom queries using @Query annotation if needed. By extending JpaRepository, we can easily perform CRUD operations on the Student entity without having to write any boilerplate code.
@Repository
//perform sql without using sql
//it helps us to interact with the database, it provides all the functionalities of a jpa repository, such as saving, updating, deleting and finding entities, it also provides pagination and sorting capabilities, and it also allows us to define custom queries using JPQL or native SQL. By extending JpaRepository, we can easily perform CRUD operations on the Student entity without having to write any boilerplate code.
//generates sql tabels and operations automatically.
public interface StudentRepository extends JpaRepository<Student, Integer> {
    
}
