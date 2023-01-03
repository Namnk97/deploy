package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Customers_Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Customers_GroupRepository extends JpaRepository<Customers_Groups, Long> {

    List<Customers_Groups> findByDeleted(Integer deleted);

    Customers_Groups findByName(String name);

    Customers_Groups findByNameAndDeleted(String name,Integer deleted);


}
