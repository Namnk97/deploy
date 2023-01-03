package com.nextsol.khangbb.controller;


import com.nextsol.khangbb.model.ReportImportBranch;
import com.nextsol.khangbb.service.CodeDetailService;
import com.nextsol.khangbb.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/code")
public class CodeAPI {

    private final CodeService codeService;

    private final CodeDetailService codeDetailService;

    @GetMapping
    public ResponseEntity<?> rentCode(@RequestParam String action) {
        return ResponseEntity.ok(this.codeService.rentcode(action));
    }

    @PostMapping("/report")
    public ResponseEntity<?> displayReport(@RequestBody ReportImportBranch dto, Pageable pageable) {
        return ResponseEntity.ok(this.codeService.displayReport(dto, pageable));
    }

    @GetMapping("/display-lcn")
    public ResponseEntity<?> displayLCN() {
        return ResponseEntity.ok(this.codeService.displayLCN());
    }

    @GetMapping("/display-detailcode")
    public ResponseEntity<?> displayDetailCode(@RequestParam Long idCode) {
        return ResponseEntity.ok(this.codeDetailService.displayDetail(idCode));
    }

    @PostMapping("/save-lcn")
    public ResponseEntity<?> saveLCN(@RequestParam Long idCode) {
        return ResponseEntity.ok(this.codeDetailService.saveProductLCN(idCode));
    }

    @PostMapping("/reject-lcn")
    public ResponseEntity<?> rejectLCN(@RequestParam Long idCode, @RequestBody String reason) {
        return ResponseEntity.ok(this.codeDetailService.rejectProductLCN(idCode, reason));
    }
}
