package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {


    List<Branch> findByDeleted(Integer deleted);

    Branch findByIdAndDeleted(Long id, Integer deleted);

    Branch findByNameAndDeleted(String name,Integer deleted);




}
