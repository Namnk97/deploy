package com.nextsol.khangbb.util;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@UtilityClass
public class CommonUtil {

    public <T> Page<T> getPageImpl(EntityManager em, String sql, Map<String, Object> params, Pageable pageable,
                                   String resultMapping) {
        return getPageImpl(em, sql, "SELECT Count(*) from (" + sql + ") a ", params, pageable, resultMapping);
    }

    public <T> Page<T> getPageImpl(EntityManager em, String sql, String sqlCount, Map<String, Object> params,
                                   Pageable pageable, String resultMapping) {
        Query countQuery = em.createNativeQuery(sqlCount);
        Query query = em.createNativeQuery(sql, resultMapping);
        if (!pageable.isUnpaged()) {
            query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());
        }
        if (!DataUtil.isNullOrEmpty(params)) {
            setQueryParams(query, countQuery, params);
        }
        List<T> result = query.getResultList();

        return new PageImpl<>(result, pageable, ((BigInteger) countQuery.getSingleResult()).intValue());
    }

    public void setQueryParams(Query query, Query countQuery, Map<String, Object> params) {
        if (null == params || params.isEmpty()) {
            return;
        }
        params.forEach((k, v) -> {
            query.setParameter(k, v);
            countQuery.setParameter(k, v);
        });
    }

    public <T> List<T> getList(EntityManager em, String sql, String sqlCount, Map<String, Object> params,
                               String resultMapping) {
        Query countQuery = em.createNativeQuery(sqlCount);
        Query query = em.createNativeQuery(sql, resultMapping);
        if (!DataUtil.isNullOrEmpty(params)) {
            setQueryParams(query, countQuery, params);
        }
        List<T> result = query.getResultList();

        return result;

    }

    public <T> List<T> getList(EntityManager em, String sql, Map<String, Object> params, String resultMapping) {
        return getList(em, sql, "SELECT Count(*) from (" + sql + ") a ", params, resultMapping);
    }

    public Stream<Object> flatten(Object[] array) {
        return Arrays.stream(array)
                .flatMap(o -> o instanceof Object[]? flatten((Object[])o): Stream.of(o));
    }

    public <T> List<T> flattenListOfListsStream(Collection<List<T>> list) {
        return list.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static int getNumberOfNonEmptyCells(XSSFSheet sheet, int columnIndex) {
        int numOfNonEmptyCells = 0;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                XSSFCell cell = row.getCell(columnIndex);
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    numOfNonEmptyCells++;
                }
            }
        }
        return numOfNonEmptyCells;
    }
}
