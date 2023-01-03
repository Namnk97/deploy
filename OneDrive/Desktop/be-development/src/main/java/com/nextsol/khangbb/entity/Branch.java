package com.nextsol.khangbb.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_branch")
public class Branch {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String address;
    private String phone;
    private String email;
    private String district;
    private String city;
    private Integer actived;
    private Integer deleted;


    @OneToMany(mappedBy = "pointConfig", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<Point_Config> configList;

    @OneToMany(mappedBy = "branch", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<Users> usersList;

    @OneToMany(mappedBy = "branch", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<Products> productsList;
}
