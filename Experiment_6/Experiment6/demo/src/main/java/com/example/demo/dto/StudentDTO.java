package com.example.demo.dto;
// data transfer object, is used to transfer data between different layers of an application, such as between the service layer and the controller layer. It is a simple Java class that contains fields and their corresponding getters and setters, but does not contain any business logic or behavior. The purpose of a DTO is to encapsulate the data and provide a structured way to transfer it across different parts of the application, often for the purpose of improving performance and reducing coupling between layers.
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class StudentDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "course cant be blank")
    private String course;
}
