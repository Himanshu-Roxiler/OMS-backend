package com.roxiler.erp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Entity
@Table(name = "organization")
@Data
public class Organization extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @NotBlank(message = "Organization name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="name")
    private String name;

    @NotBlank(message = "Address should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="address")
    private String address;

    @NotBlank(message = "City should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="city")
    private String city;

    @NotBlank(message = "State should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="state")
    private String state;

    @NotBlank(message = "Country should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="country")
    private String country;

    @OneToMany(
            mappedBy = "organization",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = Users.class
    )
    private Set<Users> users;

    @OneToMany(
            mappedBy = "organization",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = Department.class
    )
    private Set<Department> departments;

    @OneToMany(
            mappedBy = "organization",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = Designation.class
    )
    private Set<Designation> designations;

    @OneToMany(
            mappedBy = "organization",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = UserRole.class
    )
    private Set<UserRole> roles;
}
