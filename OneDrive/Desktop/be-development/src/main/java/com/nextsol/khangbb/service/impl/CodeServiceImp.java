package com.nextsol.khangbb.service.impl;


import com.nextsol.khangbb.entity.*;
import com.nextsol.khangbb.model.CodeDTO;
import com.nextsol.khangbb.model.ReportImportBranch;
import com.nextsol.khangbb.repository.*;
import com.nextsol.khangbb.service.CodeService;
import com.nextsol.khangbb.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CodeServiceImp implements CodeService {


    private final CodeRepository codeRepository;
    private final UserRepository userRepository;

    @Override
    public String rentcode(String type) {
        Users users = getInfoUser();
        List<Long> ids = this.codeRepository.findFirstIdByType(type);
        if (ids.isEmpty())
            return type + "." + users.getBranch().getId() + ".1";
        //
        Long id = ids.get(0);
        Code code = codeRepository.findById(id).orElse(null);
        assert code != null;
        String count = code.getCode().split("\\.")[2];
        int raw = Integer.parseInt(count);
        raw++;
        return type + "." + users.getBranch().getId() + "." + raw;

    }

    @Override
    public Page<ReportImportBranch> displayReport(ReportImportBranch dto, Pageable pageable) {

        return this.codeRepository.reportBranch(dto, pageable);
    }

    @Override
    public List<CodeDTO> displayLCN() {
        Users users = getInfoUser();
        List<Code> codeList = this.codeRepository.findByCodeContainingAndBranchTo("LCN.", users.getBranch().getId());
        List<CodeDTO> dtoList = MapperUtil.mapList(codeList, CodeDTO.class);
        return dtoList;
    }

    public Users getInfoUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(null);
    }

}