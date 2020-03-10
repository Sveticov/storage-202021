package com.svetikov.storage2020.service;

import com.svetikov.storage2020.models.PLCDbData;
import com.svetikov.storage2020.repository.PLCDBRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@Qualifier("db")
public class PLCDBService implements ModelService<PLCDbData, Integer> {
    private final PLCDBRepository plcdbRepository;

    @Autowired
    public PLCDBService(PLCDBRepository plcdbRepository) {
        this.plcdbRepository = plcdbRepository;
    }

    @Override
    public PLCDbData saveModel(PLCDbData plcDbData) {
        log.info("create: "+plcDbData.toString());
        return plcdbRepository.save(plcDbData);
    }

    @Override
    public PLCDbData getModelByID(Integer id) {
        log.info("get id"+plcdbRepository.findById(id).orElseGet(null));
        return plcdbRepository.findById(id).orElseGet(null);
    }

    @Override
    public List<PLCDbData> getAllModel() {
        log.info("get all plc db");
        return plcdbRepository.findAll();
    }

    @Override
    public PLCDbData deleteModel(Integer id) {
        PLCDbData plcDbData=plcdbRepository.findById(id).orElseGet(null);
        plcdbRepository.delete(plcDbData);
        return plcDbData;
    }
}
