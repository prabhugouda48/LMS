package com.lms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    @ToString.Exclude
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    @OneToMany(mappedBy = "instructor")
    @ToString.Exclude
    private Set<Course> instructedCourses = new HashSet<>();
    
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<Enrollment> enrollments = new HashSet<>();
    
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<AssignmentSubmission> submissions = new HashSet<>();
    
    public enum UserRole {
        ADMIN,
        INSTRUCTOR,
        STUDENT
    }
}
