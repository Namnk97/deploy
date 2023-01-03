package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long>, CodeRepositoryCustom {

    @Query("SELECT b.id FROM Code b " +
            " WHERE b.code LIKE %:code% ORDER BY b.id DESC")
    List<Long> findFirstIdByType(@Param("code") String code);


    List<Code> findByCodeContainingAndBranchTo(String code, Long idBranchTo);

}
