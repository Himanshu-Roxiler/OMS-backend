package com.roxiler.erp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.Date;

@Entity
@Table(name = "user_profile")
@Where(clause = "deleted_at IS NULL")
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserProfile extends BaseEntity {

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

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth", nullable = true)
    private Date dateOfBirth;

    @Column(name = "gender", nullable = true)
    private String gender;

    @Column(name = "work_phone_number", nullable = true)
    private String workPhoneNumber;

    @Column(name = "personal_phone_number", nullable = true)
    private String personalPhoneNumber;

    @Column(name = "aadhar_card", nullable = true)
    private String aadharCard;

    @Column(name = "pan_card", nullable = true)
    private String panCard;

    @Column(name = "monthly_ctc", nullable = true)
    private String monthlyCtc;

    @Column(name = "language", nullable = true)
    private String language;

    //    @JsonIdentityInfo(
//            generator = ObjectIdGenerators.PropertyGenerator.class,
//            property = "id")
    @JsonIgnore
    @OneToOne(
            mappedBy = "userProfile",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            targetEntity = Users.class
    )
    @JoinColumn(name = "user", referencedColumnName = "userProfile")
    private Users user;
}


