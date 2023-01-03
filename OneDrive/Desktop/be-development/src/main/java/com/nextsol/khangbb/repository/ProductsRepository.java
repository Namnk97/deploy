package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Branch;
import com.nextsol.khangbb.entity.Product_Groups;
import com.nextsol.khangbb.entity.Products;
import com.nextsol.khangbb.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long>, ProductRepositoryCustom {

    Page<Products> findByBranch(Branch branch, Pageable pageable);

    Optional<Products> findByDeletedAndCode(Integer deleted, String code);

    List<Products> findByBranchAndDeleted(Branch branch, Integer deleted);

    List<Products> findByBranch(Branch branch);

    @Query("SELECT p FROM Products p WHERE (CONCAT(p.name,p.code) LIKE %:request%) AND p.deleted = 0 AND (p.branch = :id OR :id IS NULL)")
    List<Products> findByNameOrCode(
            @Param("request") String request,
            @Param("id") @Nullable Branch branch
    );


    @Query("SELECT p FROM Products p WHERE (CONCAT(p.name,p.code) LIKE %:request%) AND p.deleted = 0 AND (p.branch = :id)")
    List<Products> findByNameOrCodeFromBranchUser(
            @Param("request") String request,
            @Param("id") Branch branch
    );

    @Query("SELECT p FROM Products p WHERE (CONCAT(p.name,p.code) LIKE %:request%) AND p.deleted = 0")
    Page<Products> findByNameOrCodeApp(
            Pageable pageable,
            @Param("request") String request
    );

    @Query("SELECT p FROM Products p WHERE p.deleted = 0 AND (p.productGroup = :group OR :group IS NULL) AND p.branch = :id AND p.quantity > 0")
    List<Products> findByGroupQuantity(
            @Param("group") @Nullable Product_Groups product_groups,
            @Param("id") Branch branch);

    @Query("SELECT p FROM Products p WHERE p.deleted = 0 AND p.branch IS NULL")
    List<Products> findByDeletedAndBranch();

    @Query(value = "SELECT * from tbl_product p WHERE p.deleted =0 AND p.branch_id IS NULL ", nativeQuery = true)
    List<Products> findByAll();

    Optional<Products> findByNameAndCodeAndBranch(String name, String code, Branch branch);

    Page<Products> findByDeleted(Integer deleted, Pageable pageable);

    Optional<Products> findByCodeAndNameAndImportPriceAndBranch(String code, String name, BigDecimal price, Branch branch);

    List<Products> findByDeleted(Integer integer);

    @Query("Select  p FROM Products p order by p.updatedDate desc ")
    List<Products> findByUpdatedDate();

    @Query("select p from Products p where p.id = :id and p.quantity >= :quantity")
    List<Products> checkQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);

    List<Products> findByIdIn(List<Long> id);

    @Query("SELECT p FROM Products p, Code c " +
            "WHERE p.deleted = 0 and p.quantity >0 " +
            " and (c.supplier = :ncc or :ncc IS NULL) group by p.id ")
    List<Products> findSupplier(@Param("ncc") Supplier ncc);


}
