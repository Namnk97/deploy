package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Role_Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Role_PermissionRepository extends JpaRepository<Role_Permission,Long> {
}
