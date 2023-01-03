package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Customers;
import com.nextsol.khangbb.entity.Customers_Groups;
import com.nextsol.khangbb.model.Customer.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long>, CustomerRepositoryCustom {

    Page<Customers> findByDeleted(Integer deleted, Pageable pageable);

    @Query("SELECT new com.nextsol.khangbb.model.Customer.CustomerResponse (c.id, CONCAT(c.fullname,' | ',c.phone))" +
            " FROM Customers c WHERE (c.fullname LIKE %:request% OR c.phone LIKE %:request%)" +
            " AND c.deleted = 0")
    List<CustomerResponse> findByNameOrPhone(@Param("request") String request);

    List<Customers> findByDeleted(Integer deleted);


    Integer countByCustomerGroup(Customers_Groups customersGroups);

    Customers findByPhone(String phone);

    Customers findByPhoneAndDeleted(String phone,Integer deleted);

    List<Customers> findByIdIn(List<Long> id);

    @Query("SELECT p FROM Customers p WHERE (CONCAT(p.fullname,p.phone) LIKE %:request%) AND p.deleted = 0")
    List<Customers> findByFullNameOrPhone(@Param("request") String request);



}
