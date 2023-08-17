package com.roxiler.erp.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "user_organization_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserOrganizationRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = true
    )
    private Users user;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "organization_id",
            referencedColumnName = "id",
            nullable = true
    )
    private Organization organization;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(
            name = "role_id",
            referencedColumnName = "id",
            nullable = true
    )
    private UserRole role;
}
