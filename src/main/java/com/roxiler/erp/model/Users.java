package com.roxiler.erp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Where(clause = "deleted_at IS NULL")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "First name should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "First name should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "Username should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    @Column(name = "username")
    private String username;

    @NotBlank(message = "Email should not be blank")
    @Email(message = "Invalid Email ID")
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank(message = "Password should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    @Column(name = "password")
    private String password;

    @Column(name = "password_reset_token", nullable = true)
    private String passwordResetToken;

    @Column(name = "active_organization", nullable = true)
    private Integer activeOrganization;

    @Column(name = "google_id")
    private String googleId;

    @Column(name = "outlook_id")
    private String outlookId;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "organization",
            referencedColumnName = "id",
            nullable = true
    )
    private Organization organization;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "department",
            referencedColumnName = "id",
            nullable = true
    )
    private Department department;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "designation",
            referencedColumnName = "id",
            nullable = true
    )
    private Designation designation;

//    @OneToMany(
//            //mappedBy = "users",
//            fetch = FetchType.EAGER,
//            cascade = CascadeType.ALL
//            //targetEntity = UserRole.class
//    )
//    @JoinColumn(name = "roles", referencedColumnName = "id")
//    private Set<UserRole> roles = new HashSet<>();

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST,
            targetEntity = UserOrganizationRole.class
    )
    private Set<UserOrganizationRole> userOrganizationRole = new HashSet<>();

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "user_profile",
            referencedColumnName = "id",
            nullable = true
    )
    private UserProfile userProfile;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "user_leaves",
            referencedColumnName = "id",
            nullable = true
    )
    private Leaves userLeaves;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "reporting_manager",
            referencedColumnName = "id",
            nullable = true
    )
    private Users reportingManager;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST,
            targetEntity = LeavesTracker.class
    )
    private Set<LeavesTracker> userLeaveTracker = new HashSet<>();
}
