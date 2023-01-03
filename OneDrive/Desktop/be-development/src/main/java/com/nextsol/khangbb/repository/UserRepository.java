package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>, UserRepositoryCustom {
    Optional<Users> findByUsernameAndDeleted(String username, Integer deleted);

    Page<Users> findByDeletedAndBranch(Integer deleted, Branch branch, Pageable pageable);

    Users findByIdAndDeleted(Long id, Integer deleted);

}
