package com.example.wschat.model.entity;

import com.example.wschat.model.enums.UserStatuses;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number" , unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "status")
    private UserStatuses status;

    @Column(name = "created_on",insertable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "last_seen_on")
    private LocalDateTime lastSeenOn;

}
