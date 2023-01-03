package com.nextsol.khangbb.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_role")
public class Roles implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String code;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<Role_Permission> role_permissions;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<Users> users;

}
