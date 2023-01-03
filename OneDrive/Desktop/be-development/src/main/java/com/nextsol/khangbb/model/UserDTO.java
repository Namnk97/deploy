package com.nextsol.khangbb.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.nextsol.khangbb.entity.Branch;
import com.nextsol.khangbb.entity.Screen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO extends BaseModel {
    private String fullname;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String username;
    private Long idBranch;
    private Long idRole;
    private Branch branch;
    private List<Screen> screens;
    private String token;

    public UserDTO(String fullname, String username, Long idBranch, Long idRole) {
        this.fullname = fullname;
        this.username = username;
        this.idBranch = idBranch;
        this.idRole = idRole;
    }
}
