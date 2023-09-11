package com.roxiler.erp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "organization")
@Where(clause = "deleted_at IS NULL")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer"})
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
public class Organization extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Organization name should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Address should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    @Column(name = "address")
    private String address;

    @NotBlank(message = "City should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    @Column(name = "city")
    private String city;

    @NotBlank(message = "State should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    @Column(name = "state")
    private String state;

    @NotBlank(message = "Country should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    @Column(name = "country")
    private String country;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToMany(
            mappedBy = "organization",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = Users.class
    )
    private Set<Users> users = new HashSet<>();

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToMany(
            mappedBy = "organization",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = Department.class
    )
    //@JsonManagedReference
    private Set<Department> departments = new HashSet<>();

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToMany(
            mappedBy = "organization",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = Designation.class
    )
    private Set<Designation> designations = new HashSet<>();

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToMany(
            mappedBy = "organization",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = UserRole.class
    )
    private Set<UserRole> roles = new HashSet<>();

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToMany(
            mappedBy = "organization",
            fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST,
            targetEntity = UserOrganizationRole.class
    )
    private Set<UserOrganizationRole> userOrganizationRole = new HashSet<>();

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToMany(
            mappedBy = "organization",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = LeavesSystem.class
    )
    private Set<LeavesSystem> leavesSystems = new HashSet<>();

    @OneToMany(
            mappedBy = "organization",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = Holiday.class
    )
    @JsonBackReference
    private Set<Holiday> holidays = new HashSet<>();
}
