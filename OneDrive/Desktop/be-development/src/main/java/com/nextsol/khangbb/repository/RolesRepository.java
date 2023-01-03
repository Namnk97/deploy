package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

//    List<Roles> findByName();
}
