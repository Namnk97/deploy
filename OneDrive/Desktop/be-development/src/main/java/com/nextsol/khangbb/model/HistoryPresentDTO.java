package com.nextsol.khangbb.model;

import com.nextsol.khangbb.entity.Present;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryPresentDTO {
    private Present present;
    private Date receiveAt;
}
