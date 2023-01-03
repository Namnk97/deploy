package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.model.CustomerDTO;
import com.nextsol.khangbb.util.CommonUtil;
import com.nextsol.khangbb.util.DataUtil;
import com.nextsol.khangbb.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Page<CustomerDTO> findRequirementCustomer(CustomerDTO dto, Pageable pageable) {
        StringBuilder sql = new StringBuilder("select ")
                .append("i.id,i.fullname,i.phone,i.address,i.email,i.birthday,i.turnover,i.number_purchase,i.point,i.debit,i.description,i.group_id")
                .append(" from tbl_customer i ")
                .append(" where i.deleted =0 ");

        Map<String, Object> params = new HashMap<>();
        if (!DataUtil.isNullObject(dto.getFullname())) {
            sql.append(" and lower(i.fullname) like :fullname ");
            params.put("fullname", "%" + dto.getFullname().toLowerCase() + "%");
        }

        if (!DataUtil.isNullObject(dto.getIdGroup())) {
            sql.append(" and i.group_id = :groupId ");
            params.put("groupId", dto.getIdGroup());
        }

        if (!DataUtil.isNullObject(dto.getTurnoverFrom()) && !DataUtil.isNullObject(dto.getTurnoverTo())) {
            sql.append(" and i.turnover between :turnoverFrom and :turnoverTo ");
            params.put("turnoverFrom", dto.getTurnoverFrom());
            params.put("turnoverTo", dto.getTurnoverTo());
        }
        if (!DataUtil.isNullObject(dto.getTurnoverFrom()) && DataUtil.isNullObject(dto.getTurnoverTo())) {
            sql.append(" and i.turnover >= :turnoverFrom ");
            params.put("turnoverFrom", dto.getTurnoverFrom());
        }

        if (DataUtil.isNullObject(dto.getTurnoverFrom()) && !DataUtil.isNullObject(dto.getTurnoverTo())) {
            sql.append(" and i.turnover <= :turnoverTo ");
            params.put("turnoverTo", dto.getTurnoverTo());
        }


        if (!DataUtil.isNullObject(dto.getDebitFrom()) && !DataUtil.isNullObject(dto.getDebitTo())) {
            sql.append(" and i.debit between :debitFrom and :debitTo ");
            params.put("debitFrom", dto.getDebitFrom());
            params.put("debitTo", dto.getDebitTo());
        }
        if (!DataUtil.isNullObject(dto.getDebitFrom()) && DataUtil.isNullObject(dto.getDebitTo())) {
            sql.append(" and i.debit >= :debitFrom  ");
            params.put("debitFrom", dto.getDebitFrom());

        }
        if (DataUtil.isNullObject(dto.getDebitFrom()) && !DataUtil.isNullObject(dto.getDebitTo())) {
            sql.append(" and i.debit <= :debitTo ");
            params.put("debitTo", dto.getDebitTo());
        }


        if (!DataUtil.isNullObject(dto.getPointFrom()) && !DataUtil.isNullObject(dto.getPointTo())) {
            sql.append(" and i.point between :pointFrom and :pointTo ");
            params.put("pointFrom", dto.getPointFrom());
            params.put("pointTo", dto.getPointTo());
        }

        if (!DataUtil.isNullObject(dto.getPointFrom()) && DataUtil.isNullObject(dto.getPointTo())) {
            sql.append(" and i.point >= :pointFrom  ");
            params.put("pointFrom", dto.getPointFrom());

        }
        if (DataUtil.isNullObject(dto.getPointFrom()) && !DataUtil.isNullObject(dto.getPointTo())) {
            sql.append(" and i.point <= :pointTo ");
            params.put("pointTo", dto.getPointTo());
        }

        if (!DataUtil.isNullObject(dto.getPhone())) {
            sql.append(" and lower(i.phone) like :phone ");
            params.put("phone", "%" + dto.getPhone().toLowerCase() + "%");
        }

        if (!DataUtil.isNullObject(dto.getNumberPurchaseFrom()) && !DataUtil.isNullObject(dto.getNumberPurchaseTo())) {
            sql.append(" and i.number_purchase between :numberPurchaseFrom and :numberPurchaseTo ");
            params.put("numberPurchaseFrom", dto.getNumberPurchaseFrom());
            params.put("numberPurchaseTo", dto.getNumberPurchaseTo());
        }

        if (!DataUtil.isNullObject(dto.getNumberPurchaseFrom()) && DataUtil.isNullObject(dto.getNumberPurchaseTo())) {
            sql.append(" and i.number_purchase >= :numberPurchaseFrom ");
            params.put("numberPurchaseFrom", dto.getNumberPurchaseFrom());
        }

        if (DataUtil.isNullObject(dto.getNumberPurchaseFrom()) && !DataUtil.isNullObject(dto.getNumberPurchaseTo())) {
            sql.append(" and i.number_purchase =< :numberPurchaseTo ");
            params.put("numberPurchaseTo", dto.getNumberPurchaseTo());
        }


        if (!DataUtil.isNullObject(dto.getEmail())) {
            sql.append(" and lower(i.email) like :email ");
            params.put("email", "%" + dto.getEmail().toLowerCase() + "%");
        }


        if (!DataUtil.isNullObject(dto.getBirthdayFrom()) && !DataUtil.isNullObject(dto.getBirthdayTo())) {
            try {
                String from = DateUtil.sdf.format(dto.getBirthdayFrom());
                Date createFrom = DateUtil.sdf.parse(from);
                String to = DateUtil.sdf.format(dto.getBirthdayTo());
                Date createTo = DateUtil.sdf.parse(to);
                sql.append(" and i.birthday between :createdFrom and :createdTo ");
                params.put("createdFrom", createFrom);
                params.put("createdTo", createTo);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (!DataUtil.isNullObject(dto.getBirthdayFrom()) && DataUtil.isNullObject(dto.getBirthdayTo())) {
            try {
                String from = DateUtil.sdf.format(dto.getBirthdayFrom());
                Date createFrom = DateUtil.sdf.parse(from);
                sql.append(" and i.birthday between :createdFrom and CURDATE() ");
                params.put("createdFrom", createFrom);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        if (DataUtil.isNullObject(dto.getBirthdayFrom()) && !DataUtil.isNullObject(dto.getBirthdayTo())) {
            try {
                String to = DateUtil.sdf.format(dto.getBirthdayTo());
                Date createTo = DateUtil.sdf.parse(to);
                sql.append(" and i.birthday < :createdTo ");
                params.put("createdTo", createTo);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }


        return CommonUtil.getPageImpl(em, sql.toString(), params, pageable, "getAllCustomer");
    }
}
