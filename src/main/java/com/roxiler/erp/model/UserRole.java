package com.roxiler.erp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
@Table(name = "user_role")
@Where(clause = "deleted_at IS NULL")
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Role name should not be blank")
    @Size(min = 3, message = "Length should not be less than 3")
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "organization",
            referencedColumnName = "id",
            nullable = true
    )
    private Organization organization;

    @ManyToMany(
            mappedBy = "roles",
            fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST
    )
    private Set<Feature> features = new HashSet<>();

    @OneToMany(
            mappedBy = "role",
            fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST,
            targetEntity = UserOrganizationRole.class
    )
    private Set<UserOrganizationRole> userOrganizationRole = new HashSet<>();
}
