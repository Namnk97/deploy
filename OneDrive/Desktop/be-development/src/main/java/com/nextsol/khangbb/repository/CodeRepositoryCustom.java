package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.model.ReportImportBranch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CodeRepositoryCustom {

    Page<ReportImportBranch> reportBranch(ReportImportBranch report, Pageable pageable);
}
