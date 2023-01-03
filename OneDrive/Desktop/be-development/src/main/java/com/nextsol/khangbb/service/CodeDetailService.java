package com.nextsol.khangbb.service;

import com.nextsol.khangbb.model.CodeDetailDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CodeDetailService {

    List<CodeDetailDTO> displayDetail(Long idCode);

    ResponseEntity<?> saveProductLCN(Long idCode);

    ResponseEntity<?> rejectProductLCN(Long idCode,String reason);
}
