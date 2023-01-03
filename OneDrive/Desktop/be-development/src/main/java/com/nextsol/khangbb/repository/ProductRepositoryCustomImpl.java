package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.model.ProductDTO;
import com.nextsol.khangbb.util.CommonUtil;
import com.nextsol.khangbb.util.DataUtil;
import com.nextsol.khangbb.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<ProductDTO> findRequirement(ProductDTO productDTO, Pageable pageable) {
        StringBuilder sql = new StringBuilder("select ")
                .append("i.id,i.name,i.code,i.import_price importPrice,i.whole_price,i.retail_price,i.quantity,i.unit,i.actived,i.group_id,i.created_date createdDate,i.branch_id,i.note ")
                .append(" from tbl_product i ")
                .append(" where i.deleted =0 ");

        Map<String, Object> params = new HashMap<>();
        if (!DataUtil.isNullObject(productDTO.getName())) {
            sql.append(" and lower(i.name) like :name ");
            params.put("name", "%" + productDTO.getName().toLowerCase() + "%");
        }

        if (!DataUtil.isNullObject(productDTO.getCreateFrom()) && !DataUtil.isNullObject(productDTO.getCreateTo())) {
            try {
                String from = DateUtil.sdf.format(productDTO.getCreateFrom());
                Date createFrom = DateUtil.sdf.parse(from);
                String to = DateUtil.sdf.format(productDTO.getCreateTo());
                Date createTo = DateUtil.sdf.parse(to);
                sql.append(" and i.created_date between :createdFrom and :createdTo ");
                params.put("createdFrom", createFrom);
                params.put("createdTo", createTo);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (!DataUtil.isNullObject(productDTO.getCreateFrom()) && DataUtil.isNullObject(productDTO.getCreateTo())) {
            try {
                String from = DateUtil.sdf.format(productDTO.getCreateFrom());
                Date createFrom = DateUtil.sdf.parse(from);
                sql.append(" and i.created_date between :createdFrom and CURDATE() ");
                params.put("createdFrom", createFrom);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        if (!DataUtil.isNullObject(productDTO.getIdGroup())) {
            sql.append(" and i.group_id = :groupId ");
            params.put("groupId", productDTO.getIdGroup());
        }


        if (!DataUtil.isNullObject(productDTO.getCode())) {
            sql.append(" and lower(i.code) like :code ");
            params.put("code", "%" + productDTO.getCode().toLowerCase() + "%");
        }

        if (!DataUtil.isNullObject(productDTO.getActived())) {
            sql.append(" and i.actived = :actived ");
            params.put("actived", productDTO.getActived());
        }
        return CommonUtil.getPageImpl(em, sql.toString(), params, pageable, "getAllProduct");
    }

    @Override
    public List<ProductDTO> findByImportWarehouse(ProductDTO productDTO) {
        StringBuilder sql = new StringBuilder("select ")
                .append("i.id,i.name,i.code,i.import_price importPrice,i.whole_price,i.retail_price,i.quantity,i.unit,i.actived,i.group_id,i.created_date createdDate,i.branch_id,i.note ")
                .append(" from tbl_product i ")
                .append(" where i.deleted =0 ");

        Map<String, Object> params = new HashMap<>();
        if (!DataUtil.isNullObject(productDTO.getName())) {
            sql.append(" and lower(i.name) like :name ");
            params.put("name", "%" + productDTO.getName().toLowerCase() + "%");
        }

        if (!DataUtil.isNullObject(productDTO.getCreateFrom()) && !DataUtil.isNullObject(productDTO.getCreateTo())) {
            try {
                String from = DateUtil.sdf.format(productDTO.getCreateFrom());
                Date createFrom = DateUtil.sdf.parse(from);
                String to = DateUtil.sdf.format(productDTO.getCreateTo());
                Date createTo = DateUtil.sdf.parse(to);
                sql.append(" and i.created_date between :createdFrom and :createdTo ");
                params.put("createdFrom", createFrom);
                params.put("createdTo", createTo);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        if (!DataUtil.isNullObject(productDTO.getCreateFrom()) && DataUtil.isNullObject(productDTO.getCreateTo())) {
            try {
                String from = DateUtil.sdf.format(productDTO.getCreateFrom());
                Date createFrom = DateUtil.sdf.parse(from);
                sql.append(" and i.created_date between :createdFrom and CURDATE()");
                params.put("createdFrom", createFrom);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        if (!DataUtil.isNullObject(productDTO.getIdGroupStore()) && productDTO.getIdGroupStore().size() != 0) {
            sql.append(" and i.group_id IN :idGroupStore");
            params.put("idGroupStore", productDTO.getIdGroupStore());

        }


        if (!DataUtil.isNullObject(productDTO.getCode())) {
            sql.append(" and lower(i.code) like :code ");
            params.put("code", "%" + productDTO.getCode().toLowerCase() + "%");
        }

        if (!DataUtil.isNullObject(productDTO.getIdBranch())) {
            sql.append(" and i.branch_id = :branchId ");
            params.put("branchId", productDTO.getIdBranch());
        }

        if (!DataUtil.isNullObject(productDTO.getQuantityFrom()) && !DataUtil.isNullObject(productDTO.getQuantityTo())) {
            sql.append(" and i.quantity between :quantityFrom and :quantityTo ");
            params.put("quantityFrom", productDTO.getQuantityFrom());
            params.put("quantityTo", productDTO.getQuantityTo());
        }

        if (!DataUtil.isNullObject(productDTO.getQuantityFrom()) && DataUtil.isNullObject(productDTO.getQuantityTo())) {
            sql.append(" and i.quantity >= :quantityFrom ");
            params.put("quantityFrom", productDTO.getQuantityFrom());
        }

        if (DataUtil.isNullObject(productDTO.getQuantityFrom()) && !DataUtil.isNullObject(productDTO.getQuantityTo())) {
            sql.append(" and i.quantity <= :quantityTo ");
            params.put("quantityTo", productDTO.getQuantityTo());
        }


        return CommonUtil.getList(em, sql.toString(), params, "getAllProduct");
    }
}
