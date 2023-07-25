package com.roxiler.erp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user_profile")
@Data
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    @NotBlank(message = "First name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="first_name")
    private String firstName;

    @NotBlank(message = "First name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="last_name")
    private String lastName;
}
