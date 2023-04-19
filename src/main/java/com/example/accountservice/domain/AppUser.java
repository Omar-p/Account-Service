package com.example.accountservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(generator = "app_user_id_seq",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name = "app_user_id_seq",
            sequenceName = "app_user_id_seq",
            allocationSize = 1
    )
    private Long id;

}
