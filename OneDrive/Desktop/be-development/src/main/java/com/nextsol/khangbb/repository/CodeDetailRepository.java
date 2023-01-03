package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Code;
import com.nextsol.khangbb.entity.Code_Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CodeDetailRepository extends JpaRepository<Code_Detail, Long> {

    List<Code_Detail> findByCode(Code code);
}
