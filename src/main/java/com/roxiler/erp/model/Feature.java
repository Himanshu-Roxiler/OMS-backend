package com.roxiler.erp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "feature")
@Where(clause = "deleted_at IS NULL")
@Data
public class Feature extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @NotBlank(message = "Role name should not be blank")
    @Size(min=3, message = "Length should not be less than 3")
    @Column(name="name")
    private String name;

    @Column(name="display_name", nullable = true)
    private String displayName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name="feature_role",
            joinColumns = {
                    @JoinColumn(name="feature_id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name="role_id")
            }
    )
    private Set<UserRole> roles = new HashSet<>();
}
