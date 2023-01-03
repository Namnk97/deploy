package com.nextsol.khangbb.repository;


import com.nextsol.khangbb.model.UserDTO;
import com.nextsol.khangbb.util.CommonUtil;
import com.nextsol.khangbb.util.DataUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<UserDTO> findByRequement(UserDTO dto) {
        StringBuilder sql = new StringBuilder("select ")
                .append("i.fullname,i.username,i.branch_id,i.role_id ")
                .append(" from tbl_user i ")
                .append(" where i.deleted =0 ");

        Map<String, Object> params = new HashMap<>();
        if (!DataUtil.isNullObject(dto.getFullname())) {
            sql.append(" and lower(i.fullname) like :fullname ");
            params.put("fullname", "%" + dto.getFullname().toLowerCase() + "%");
        }

        if (!DataUtil.isNullObject(dto.getUsername())) {
            sql.append(" and lower(i.username) like :username ");
            params.put("username", "%" + dto.getUsername().toLowerCase() + "%");
        }


        if (!DataUtil.isNullObject(dto.getIdBranch())) {
            sql.append(" and i.branch_id = :branchId ");
            params.put("branchId", dto.getIdBranch());
        }

        if (!DataUtil.isNullObject(dto.getIdRole())) {
            sql.append(" and i.role_id = :roleId ");
            params.put("roleId", dto.getIdRole());
        }

        return CommonUtil.getList(em, sql.toString(), params, "getUsers");
    }
}
