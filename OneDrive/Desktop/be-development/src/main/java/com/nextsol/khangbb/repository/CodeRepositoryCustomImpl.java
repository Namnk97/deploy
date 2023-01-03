package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.model.ReportImportBranch;
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

public class CodeRepositoryCustomImpl implements CodeRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<ReportImportBranch> reportBranch(ReportImportBranch report, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT p.code as codeProduct ,p.name as nameProduct ,cd.quantity,p.import_price as importPrice ,c.created_date as createDay,c.code as codeCode,c.type as typeCode,c.note as note " +
                " FROM tbl_code c JOIN tbl_code_detail cd ON c.id = cd.code_id JOIN tbl_product p ON p.id = cd.product_id GROUP BY c.code");

        Map<String, Object> params = new HashMap<>();
        if (!DataUtil.isNullObject(report.getCodeProduct())) {
            sql.append(" and lower(p.code) like :code ");
            params.put("code", "%" + report.getCodeProduct().toLowerCase() + "%");
        }

        if (!DataUtil.isNullObject(report.getNameProduct())) {
            sql.append(" and lower(p.name) like :name ");
            params.put("name", "%" + report.getNameProduct().toLowerCase() + "%");
        }

        if (!DataUtil.isNullObject(report.getCodeCode())) {
            sql.append(" and lower(c.code) like :codecode ");
            params.put("codecode", "%" + report.getCodeCode().toLowerCase() + "%");
        }

        if (!DataUtil.isNullObject(report.getTypeCode())) {
            sql.append(" and lower(c.type) like :type ");
            params.put("type", "%" + report.getTypeCode().toLowerCase() + "%");
        }

        if (!DataUtil.isNullObject(report.getDayFrom()) && !DataUtil.isNullObject(report.getDayTo())) {
            try {
                String from = DateUtil.sdf.format(report.getDayFrom());
                Date createFrom = DateUtil.sdf.parse(from);
                String to = DateUtil.sdf.format(report.getDayTo());
                Date createTo = DateUtil.sdf.parse(to);
                sql.append(" and c.created_date between :createdFrom and :createdTo ");
                params.put("createdFrom", createFrom);
                params.put("createdTo", createTo);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (!DataUtil.isNullObject(report.getDayFrom()) && DataUtil.isNullObject(report.getDayTo())) {
            try {
                String from = DateUtil.sdf.format(report.getDayFrom());
                Date createFrom = DateUtil.sdf.parse(from);
                sql.append(" and c.created_date between :createdFrom and CURDATE() ");
                params.put("createdFrom", createFrom);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        if (DataUtil.isNullObject(report.getDayFrom()) && !DataUtil.isNullObject(report.getDayTo())) {
            try {
                String to = DateUtil.sdf.format(report.getDayTo());
                Date createTo = DateUtil.sdf.parse(to);
                sql.append(" and c.created_date < :createdTo ");
                params.put("createdTo", createTo);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }


        return CommonUtil.getPageImpl(em, sql.toString(), params, pageable, "getReport");
    }
}
