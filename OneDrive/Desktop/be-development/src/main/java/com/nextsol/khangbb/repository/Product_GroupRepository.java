package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Product_Groups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface Product_GroupRepository extends JpaRepository<Product_Groups, Long> {

    Optional<Product_Groups> findByCodeContaining(String code);
    Optional<Product_Groups> findByDeletedAndCodeOrName(Integer deleted,String code, String name);

    Optional<Product_Groups> findByDeletedAndName(Integer deleted,String name);

    Page<Product_Groups> findByDeleted(Integer deleted, Pageable pageable);

    List<Product_Groups> findByDeleted(Integer deleted);


}
