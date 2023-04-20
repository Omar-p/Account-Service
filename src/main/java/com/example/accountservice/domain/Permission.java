package com.example.accountservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
    }
)
@Getter
@Setter
public class Permission {

  @Id
  @GeneratedValue(generator = "permission_id_seq", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "permission_id_seq", sequenceName = "permission_id_seq", allocationSize = 1)
  private Long id;

  private String name;

}
