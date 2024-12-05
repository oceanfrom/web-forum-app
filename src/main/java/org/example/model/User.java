package org.example.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z]* [A-Z][a-zA-Z]*$", message = "Correct format 'First Last'")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email address")
    private String email;

    @Column(name = "registration_date")
    private Timestamp registrationDate;
}
