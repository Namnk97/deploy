package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Supplier_Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Supplier_GroupRepository extends JpaRepository<Supplier_Group, Long> {

    List<Supplier_Group> findByDeleted(Integer deleted);

    Supplier_Group findByName(String name);

    Optional<Supplier_Group> findByDeletedAndName(Integer deleted,String name);
}
