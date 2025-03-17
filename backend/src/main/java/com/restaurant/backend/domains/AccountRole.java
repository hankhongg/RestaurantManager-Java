package com.restaurant.backend.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account_role")
public class AccountRole {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_role_id_gen")
    @SequenceGenerator(name = "account_role_id_gen", sequenceName = "account_role_role_id_seq", allocationSize = 1)
    @Column(name = "role_id", nullable = false)
    private Integer id;

    @Column(name = "role_name", length = Integer.MAX_VALUE)
    private String roleName;

}