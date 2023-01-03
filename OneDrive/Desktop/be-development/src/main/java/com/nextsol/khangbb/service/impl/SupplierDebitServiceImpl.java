package com.nextsol.khangbb.service.impl;


import com.nextsol.khangbb.entity.Supplier;
import com.nextsol.khangbb.entity.SupplierDebitHistory;
import com.nextsol.khangbb.entity.Supplier_Debit;
import com.nextsol.khangbb.model.SupplierDebitDTO;
import com.nextsol.khangbb.repository.SupplierDebitHistoryRepository;
import com.nextsol.khangbb.repository.SupplierRepository;
import com.nextsol.khangbb.repository.Supplier_DebitRepository;
import com.nextsol.khangbb.service.Supplier_DebitService;
import com.nextsol.khangbb.util.MapperUtil;
import com.nextsol.khangbb.util.ResponseUtil;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.nextsol.khangbb.util.CommonUtil.getNumberOfNonEmptyCells;

@Service
public class SupplierDebitServiceImpl implements Supplier_DebitService {

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private Supplier_DebitRepository supplier_debitRepository;

	@Autowired
	private SupplierDebitHistoryRepository supplierDebitHistoryRepository;

	@Override
	public List<SupplierDebitDTO> findByDebit(Long idSupplier) {
		Supplier supplier = this.supplierRepository.findById(idSupplier).orElseThrow();
		List<Supplier_Debit> debitList = this.supplier_debitRepository.findBySupplierAndDeleted(supplier, 0);
		List<SupplierDebitDTO> dtoList = MapperUtil.mapList(debitList, SupplierDebitDTO.class);
		for (int i = 0; i < dtoList.size(); i++) {
			dtoList.get(i).setSupplierDebitHistories(supplierDebitHistoryRepository.findBySupplierDebit(debitList.get(i)));
			dtoList.get(i).setSupplier(null);
			dtoList.get(i).getSupplierDebitHistories().forEach(x -> x.setSupplierDebit(null));
		}
		return dtoList;
	}

	@Override
	public ResponseEntity<?> paymentDebit(SupplierDebitDTO supplierDebitDTO) {
		Supplier_Debit supplierDebit = supplier_debitRepository.findByIdAndDeleted(supplierDebitDTO.getId(), 0);
		if(supplierDebit == null) return ResponseUtil.notFound(null, "Không tìm thấy " + supplierDebitDTO.getId());
		long debitTotal = supplierDebit.getDebit().longValue() - supplierDebit.getPay_debit().longValue(); // Số tiền còn lai thanh toán
		long payDebit = supplierDebitDTO.getPay_debit().longValue();
		if(payDebit > debitTotal) return ResponseUtil.conflict(null, "Số tiền thanh toán <= " + debitTotal);
		long debit = debitTotal - payDebit;
		SupplierDebitHistory supplierDebitHistory = new SupplierDebitHistory();
		supplierDebitHistory.setCodeForeign(supplierDebit.getCode());
		String code = supplierDebit.getCode().split("\\.")[1] + ".";
		supplierDebitHistory.setCode("TT." + code);
		supplierDebitHistory.setMoneyPayment(payDebit);
		supplierDebitHistory.setDebit(debit);
		supplierDebitHistory.setSupplierDebit(supplierDebit);
		supplierDebitHistory.setMethod("Thanh toán");
		supplierDebitHistory = supplierDebitHistoryRepository.save(supplierDebitHistory);
		supplierDebitHistory.setCode(supplierDebitHistory.getCode() + supplierDebitHistory.getId());
		supplierDebitHistoryRepository.save(supplierDebitHistory);
		supplierDebit.setPay_debit(BigDecimal.valueOf(supplierDebit.getPay_debit().longValue() + payDebit));
		if(payDebit == debitTotal) { // thanh toán tất cả thì xóa
			supplierDebit.setDeleted(1);
		}
		supplier_debitRepository.save(supplierDebit);
		return ResponseUtil.ok(null, "Thanh toán thành công");

	}

	@Override
	public ResponseEntity<?> importExcelDebit(MultipartFile multipartFile) {
		if (multipartFile.isEmpty()) return ResponseUtil.badRequest("Vui lòng gửi file excel");
		try {
			XSSFWorkbook workBook = new XSSFWorkbook(multipartFile.getInputStream());
			XSSFSheet sheet = workBook.getSheetAt(0);
			int countSuccess = 0;
			int countError = 0;
			for (int rowIndex = 1; rowIndex < getNumberOfNonEmptyCells(sheet, 0); rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex);
				String name = String.valueOf(row.getCell(0));
				BigDecimal money = new BigDecimal(String.valueOf(row.getCell(2)));
				if(name.isEmpty()){
					countError++;
					continue;
				}
				if(money.longValue() <= 0) {
					countError++;
					continue;
				}
				Supplier supplier = supplierRepository.findByName(name);
				if(supplier == null) continue;
				Supplier_Debit supplierDebit = new Supplier_Debit();
				supplierDebit.setSupplier(supplier);
				supplierDebit.setDeleted(0);
				supplierDebit.setDebit(money);
				String code = "GNTCNCC." + supplier.getId() + ".";
				supplierDebit.setCode(code);
				supplierDebit.setPay_debit(BigDecimal.valueOf(0));
				supplierDebit = supplier_debitRepository.save(supplierDebit);
				supplierDebit.setCode(code + supplierDebit.getId());
				supplier_debitRepository.save(supplierDebit);
				countSuccess++;
			}
			String message = "Tổng số lỗi: " + countError + "\n" + "Tổng số hoàn thành: " + countSuccess;
			return ResponseUtil.ok("", message);
		} catch (Exception e) {
			return ResponseUtil.internalServerError("", e.getMessage());
		}
	}

}
