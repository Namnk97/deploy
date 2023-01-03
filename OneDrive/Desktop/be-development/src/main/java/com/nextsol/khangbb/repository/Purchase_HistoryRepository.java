package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Bill;
import com.nextsol.khangbb.entity.Customers;
import com.nextsol.khangbb.entity.Purchase_History;
import com.nextsol.khangbb.model.Customer.CustomerResponse;
import com.nextsol.khangbb.model.Report.ReportByBillResponse;
import com.nextsol.khangbb.model.Report.ReportByProductResponse;
import com.nextsol.khangbb.model.paymentSlipModel.BillResponse;
import com.nextsol.khangbb.model.paymentSlipModel.EndShiftResponse;
import com.nextsol.khangbb.model.paymentSlipModel.PurchaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface Purchase_HistoryRepository extends JpaRepository<Purchase_History, Long> {
    @Query("SELECT new com.nextsol.khangbb.model.paymentSlipModel.PurchaseResponse" +
            " (ph.id,ph.typePayment,ph.point,ph.type,ph.reason,ph.status,ph.price,ph.totalPrice," +
            " ph.orderDate,ph.receivedDate,ph.description,b.code,ph.createdDate,ph.createdBy,ph.seller, c.id, ph.endShift)" +
            " FROM Bill b, Purchase_History ph LEFT JOIN Customers c" +
            " ON ph.customer = c.id" +
            " WHERE ((ph.createdDate BETWEEN :fromDate AND :toDate) OR :fromDate IS NULL)" +
            " AND ph.branchId = :branch" +
            " AND (c.id = ph.customer OR ph.customer IS NULL)" +
            " AND (ph.type = :type OR :type IS NULL)" +
            " AND (ph.reason = :reason OR :reason IS NULL) " +
            " AND (ph.customer = :customer OR :customer IS NULL) " +
            " AND (ph.bill = b.id AND b.code LIKE %:bill%)" +
            " AND ph.status != 2" +
            " AND ph.bill = b.id GROUP BY ph.id" +
            " ORDER BY ph.createdDate DESC"
    )
    Page<PurchaseResponse> filterDTO(
            Pageable pageable,
            @RequestParam("branch") Long branch,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("type") String type,
            @Param("reason") String reason,
            @Param("customer") Customers customer,
            @Param("bill") String billId
    );

    @Query("SELECT new com.nextsol.khangbb.model.paymentSlipModel.PurchaseResponse" +
            " (ph.id,ph.typePayment,ph.point,ph.type,ph.reason,ph.status,ph.price,ph.totalPrice," +
            " ph.orderDate,ph.receivedDate,ph.description,b.code,ph.createdDate,ph.createdBy,ph.seller, c.id, ph.endShift)" +
            " FROM Bill b, Purchase_History ph LEFT JOIN Customers c" +
            " ON ph.customer = c.id" +
            " WHERE ph.id = :id AND ph.bill = b.id" +
            " AND ph.branchId = :branch "+
            " AND (c.id = ph.customer OR ph.customer IS NULL)" +
            " GROUP BY ph.id"
    )
    PurchaseResponse filterDTOById(
            @Param("branch") Long branch,
            @Param("id") Long id
    );

    @Query("SELECT ph FROM Purchase_History ph, Bill b" +
            " WHERE ((ph.createdDate BETWEEN :fromDate AND :toDate) OR :fromDate IS NULL)" +
            " AND ph.branchId = :branch "+
            " AND (ph.reason = :reason OR :reason IS NULL) " +
            " AND (ph.customer = :customer OR :customer IS NULL) " +
            " AND (ph.bill = b.id AND b.code LIKE %:bill%)" +
            " AND ph.status != 2" +
            " ORDER BY ph.createdDate DESC"
    )
    Page<Purchase_History> filter(
            Pageable pageable,
            @RequestParam("branch") Long branch,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("reason") String reason,
            @Param("customer") Customers customer,
            @Param("bill") String billId
    );

    @Query("SELECT ph.id, b.code FROM Purchase_History ph, Bill b" +
            " WHERE ph.bill = b.id" +
            " AND ph.branchId = :branch" +
            " AND ph.status = 0" +
            " AND ph.reason = com.nextsol.khangbb.constant.Constant.GET_ORDER")
    List<Object> getListOrderBill(@RequestParam("branch") Long branch);

    Purchase_History findByBillAndStatus(Bill bill, Integer status);

    @Query("SELECT NEW com.nextsol.khangbb.model.paymentSlipModel.EndShiftResponse" +
            " (es.id, es.startDate, es.endDate, ph.seller, es.beforePrice, es.afterPrice)" +
            " FROM Purchase_History ph, EndShift es " +
            " WHERE ph.endShift = es.id " +
            " AND ph.branchId = :branch "+
            " AND (ph.seller = :user OR :user IS NULL)" +
            " AND ((ph.createdDate BETWEEN :fromDate AND :toDate) OR (:fromDate IS NULL AND :toDate IS NULL))" +
            " AND ph.reason = com.nextsol.khangbb.constant.Constant.SPEND_END_SHIFT" +
            " GROUP BY es.id" +
            " ORDER BY ph.createdDate DESC")
    Page<EndShiftResponse> filterEndShift(Pageable pageable,
                                          @RequestParam("branch") Long branch,
                                          @Param("user") String user,
                                          @Param("fromDate") Date fromDate,
                                          @Param("toDate") Date toDate);

    @Query("SELECT new com.nextsol.khangbb.model.paymentSlipModel.PurchaseResponse" +
            " (ph.id,ph.typePayment,ph.point,ph.type,ph.reason,ph.status,ph.price,ph.totalPrice," +
            " ph.orderDate,ph.receivedDate,ph.description,b.code,ph.createdDate,ph.createdBy,ph.seller, c.id, ph.endShift)" +
            " FROM Bill b, Purchase_History ph LEFT JOIN Customers c" +
            " ON ph.customer = c.id" +
            " WHERE ph.endShift = :endShift " +
            " AND (c.id = ph.customer OR ph.customer IS NULL)" +
            " AND ph.reason != com.nextsol.khangbb.constant.Constant.SPEND_END_SHIFT " +
            " AND b.id = ph.bill" +
            " GROUP BY ph.id")
    Page<PurchaseResponse> endShiftDetail(Pageable pageable,
                                          @Param("endShift") Long id);

    @Query("SELECT new com.nextsol.khangbb.model.paymentSlipModel.PurchaseResponse" +
            " (ph.id,ph.typePayment,ph.point,ph.type,ph.reason,ph.status,ph.price,ph.totalPrice," +
            " ph.orderDate,ph.receivedDate,ph.description,b.code,ph.createdDate,ph.createdBy,ph.seller, c.id, ph.endShift)" +
            " FROM Bill b, Purchase_History ph LEFT JOIN Customers c" +
            " ON ph.customer = c.id" +
            " WHERE ph.seller = :user " +
            " AND ph.branchId = :branch "+
            " AND b.id = ph.bill" +
            " AND ph.endShift IS NULL" +
            " AND (ph.endLastShift = 0 OR ph.endLastShift IS NULL)" +
            " GROUP BY ph.id" +
            " ORDER BY ph.createdDate DESC"
    )
    Page<PurchaseResponse> endShiftList(Pageable pageable,
                                        @RequestParam("branch") Long branch,
                                        @Param("user") String user);

    @Query("SELECT new com.nextsol.khangbb.model.paymentSlipModel.BillResponse " +
            " (p.name, bd.quantity) " +
            " FROM Bill_Detail bd, Bill b, Products p" +
            " WHERE bd.bill = b.id AND b.code = :code " +
            " AND bd.products = p.id")
    BillResponse printfBill(@Param("code") String code);

    @Query("SELECT new com.nextsol.khangbb.model.Customer.CustomerResponse (c.id,c.fullname)" +
            " FROM Purchase_History ph, Customers c WHERE c.id = ph.customer AND ph.id = :id")
    Optional<CustomerResponse> findCustomer(Long id);

    @Query("SELECT ph.id FROM Purchase_History ph, Bill b WHERE b.code = :code AND ph.bill = b.id")
    Long findIdByCode(@Param("code") String code);
    List<Purchase_History> findByCustomerAndStatus(Customers customers, Integer status);

    List<Purchase_History> findByEndShiftIsNullAndSellerAndEndLastShiftAndBranchId(String createdBy, Integer endLastShift, Long branchId);
    List<Purchase_History> findByEndShiftIsNullAndEndLastShiftAndBranchId(Integer endLastShift, Long branchId);

    @Query("SELECT new com.nextsol.khangbb.model.Report.ReportByBillResponse" +
            " (ph.id, ph.typePayment, ph.status, ph.price, ph.totalPrice, b.code, ph.createdDate, ph.seller, c.fullname, SUM(bd.discountMoney), b.discountMoney)" +
            " FROM Bill b, Bill_Detail bd, Products p, Purchase_History ph LEFT JOIN Customers c" +
            " ON ph.customer = c.id" +
            " WHERE ph.bill = b.id" +
            " AND b.id = bd.bill AND p.id = bd.products" +
            " AND ph.reason IN :list" +
            " AND (ph.createdDate BETWEEN :fromDate AND :toDate OR (:fromDate IS NULL AND :toDate IS NULL))" +
            " AND b.code LIKE %:code%" +
            " AND c.fullname LIKE %:customer%" +
            " AND ph.seller LIKE %:seller%" +
            " AND p.code LIKE %:productId%" +
            " AND p.name LIKE %:productName%" +
            " AND ph.branchId = :branch")
    Page<ReportByBillResponse> getReportByBill(
            Pageable pageable,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("code") String code,
            @Param("customer") String customer,
            @Param("seller") String seller,
            @Param("productId") String productId,
            @Param("productName") String productName,
            @Param("branch") Long branch,
            @Param("list") List<String> list
            );

    @Query("SELECT new com.nextsol.khangbb.model.Report.ReportByProductResponse" +
            " (ph.id, ph.typePayment, ph.status, p.code, p.name, p.importPrice, bd.quantity, ph.totalPrice, b.code, ph.createdDate, ph.seller, c.fullname, SUM(bd.discountMoney), b.discountMoney)" +
            " FROM Bill b, Bill_Detail bd, Products p, Purchase_History ph LEFT JOIN Customers c" +
            " ON ph.customer = c.id" +
            " WHERE ph.bill = b.id" +
            " AND b.id = bd.bill AND p.id = bd.products" +
            " AND ph.reason IN :list" +
            " AND (ph.createdDate BETWEEN :fromDate AND :toDate OR (:fromDate IS NULL AND :toDate IS NULL))" +
            " AND b.code LIKE %:code%" +
            " AND c.fullname LIKE %:customer%" +
            " AND ph.seller LIKE %:seller%" +
            " AND p.code LIKE %:productId%" +
            " AND p.name LIKE %:productName%" +
            " AND ph.branchId = :branch")
    Page<ReportByProductResponse> getReportByProduct(
            Pageable pageable,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("code") String code,
            @Param("customer") String customer,
            @Param("seller") String seller,
            @Param("productId") String productId,
            @Param("productName") String productName,
            @Param("branch") Long branch,
            @Param("list") List<String> list
    );
}
