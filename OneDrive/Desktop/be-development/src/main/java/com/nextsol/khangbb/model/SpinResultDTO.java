package com.nextsol.khangbb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nextsol.khangbb.entity.CustomerPresent;
import com.nextsol.khangbb.entity.Present;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpinResultDTO {
   private Present present;
   List<HistoryPresentDTO> historyPresents = new ArrayList<>();
}
