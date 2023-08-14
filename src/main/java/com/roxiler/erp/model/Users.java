package com.roxiler.erp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.EntityGraph;

@Entity
@Table(name = "users")
@Where(clause = "deleted_at IS NULL")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Users extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @NotBlank(message = "First name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="first_name")
    private String firstName;

    @NotBlank(message = "First name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="last_name")
    private String lastName;

    @NotBlank(message = "Username should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="username")
    private String username;

    @NotBlank(message = "Email should not be blank")
    @Email(message = "Invalid Email ID")
    @Column(name="email")
    private String email;

    @NotBlank(message = "Password should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="password")
    private String password;

    @Column(name="password_reset_token", nullable = true)
    private String passwordResetToken;

    @Column(name="active_organization", nullable = true)
    private Integer activeOrganization;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "organization",
            referencedColumnName = "id",
            nullable = true
    )
    private Organization organization;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "department",
            referencedColumnName = "id",
            nullable = true
    )
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "designation",
            referencedColumnName = "id",
            nullable = true
    )
    private Designation designation;

    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name="user_profile",
            referencedColumnName = "id",
            nullable = true
    )
    private UserProfile userProfile;
}
