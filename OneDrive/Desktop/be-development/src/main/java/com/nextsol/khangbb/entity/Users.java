package com.nextsol.khangbb.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nextsol.khangbb.model.UserDTO;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@SqlResultSetMapping(
        name = "getUsers",
        classes = {
                @ConstructorResult(
                        targetClass = UserDTO.class,
                        columns = {
                                @ColumnResult(name = "fullname", type = String.class),
                                @ColumnResult(name = "username", type = String.class),
                                @ColumnResult(name = "branch_id", type = Long.class),
                                @ColumnResult(name = "role_id", type = Long.class)
                        }
                )
        }
)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_user")
public class Users extends BaseEntity {

    private String fullname;
    private String password;
    private String username;
    private Integer deleted;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    private Roles role;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "branch_id")
    @JsonIgnore
    @ToString.Exclude
    private Branch branch;

    @OneToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "tbl_user_screen",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "screen_id", referencedColumnName = "id"))
    private List<Screen> screens;

}
