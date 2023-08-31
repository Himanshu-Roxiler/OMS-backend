package com.roxiler.erp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "designation")
@Where(clause = "deleted_at IS NULL")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer"})
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
public class Designation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Role name should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Role name should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    @Column(name = "description")
    private String description;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "organization_id",
            referencedColumnName = "id",
            nullable = true
    )
    //@JsonBackReference
    private Organization organization;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToMany(
            mappedBy = "designation",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = Users.class
    )
    private Set<Users> users = new HashSet<>();
}
