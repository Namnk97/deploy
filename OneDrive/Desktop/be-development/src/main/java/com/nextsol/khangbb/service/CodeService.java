package com.nextsol.khangbb.service;

import com.nextsol.khangbb.model.CodeDTO;
import com.nextsol.khangbb.model.ReportImportBranch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CodeService {

    String rentcode(String code);

    Page<ReportImportBranch> displayReport(ReportImportBranch dto, Pageable pageable);

    List<CodeDTO> displayLCN();


}
