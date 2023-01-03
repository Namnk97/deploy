package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.model.SupplierDTO;
import com.nextsol.khangbb.util.CommonUtil;
import com.nextsol.khangbb.util.DataUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

public class SupplierRepositoryCustomImpl implements SupplierRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<SupplierDTO> findByRequirement(SupplierDTO dto, Pageable pageable) {
        StringBuilder sql = new StringBuilder("select ")
                .append("i.id,i.name,i.address,i.contact_name contactName,i.phone,i.fax,i.email,i.description,i.group_id  ")
                .append(" from tbl_supplier i ")
                .append(" where i.deleted =0 ");

        Map<String, Object> params = new HashMap<>();
        if (!DataUtil.isNullObject(dto.getName())) {
            sql.append(" and lower(i.name) like :name ");
            params.put("name", "%" + dto.getName().toLowerCase() + "%");
        }


        if (!DataUtil.isNullObject(dto.getIdGroup())) {
            sql.append(" and i.group_id = :groupId ");
            params.put("groupId", dto.getIdGroup());
        }


        if (!DataUtil.isNullObject(dto.getPhone())) {
            sql.append(" and lower(i.phone) like :phone ");
            params.put("phone", "%" + dto.getPhone().toLowerCase() + "%");
        }
        if (!DataUtil.isNullObject(dto.getEmail())) {
            sql.append(" and lower(i.email) like :email ");
            params.put("email", "%" + dto.getEmail().toLowerCase() + "%");
        }

        return CommonUtil.getPageImpl(em, sql.toString(), params, pageable, "getAllSupplier");
    }
}
