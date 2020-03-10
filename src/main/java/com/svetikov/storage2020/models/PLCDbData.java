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
@Table(name = "plcdbdata")
public class PLCDbData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idDB;
    private String plcName;
    private String typeDB;
    private int dbAdr;
    private int dbByte;
    private int dbBit;



}
