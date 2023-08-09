package com.roxiler.erp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.Set;

@Entity
@Table(name = "department")
@Where(clause = "deleted_at IS NULL")
@Data
public class Department extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @NotBlank(message = "Role name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="name")
    private String name;

    @NotBlank(message = "Role name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "organization_id",
            referencedColumnName = "id"
    )
    private Organization organization;

    @OneToMany(
            mappedBy = "departmentId",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = Users.class
    )
    private Set<Users> users;
}
