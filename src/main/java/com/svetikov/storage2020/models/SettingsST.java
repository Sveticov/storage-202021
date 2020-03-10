package com.svetikov.storage2020.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "settingsST")
public class SettingsST implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idSettings;
    private int carBusyOffsetX;
    private int carBusyOffsetZ;
    private int storageXOffset;
    private int storageZOffset;

    public SettingsST(int carBusyOffsetX,int carBusyOffsetZ, int storageXOffset, int storageZOffset) {
        this.carBusyOffsetX=carBusyOffsetX;
        this.carBusyOffsetZ=carBusyOffsetZ;
        this.storageXOffset = storageXOffset;
        this.storageZOffset = storageZOffset;
    }
}
