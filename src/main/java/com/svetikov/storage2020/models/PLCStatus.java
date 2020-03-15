package com.svetikov.storage2020.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PLCStatus {

    private String namePLCStatus;
    private boolean statusPLCStatus;

}
