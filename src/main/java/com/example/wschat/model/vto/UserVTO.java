package com.example.wschat.model.vto;

import com.example.wschat.model.enums.UserStatuses;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private UserStatuses status;
    private LocalDateTime createdOn;
    private LocalDateTime lastSeenOn;

}
