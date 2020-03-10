package com.svetikov.storage2020.service;

import com.svetikov.storage2020.models.PLCData;
import com.svetikov.storage2020.repository.PLCRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Slf4j
@Service
@Primary
@Qualifier("plc")
public class PLCService implements ModelService<PLCData,Integer>{
//public class PLCService implements PLSServiceL {

    private PLCRepository plcRepository;

    @Autowired
    public PLCService(PLCRepository plcRepository) {
        this.plcRepository = plcRepository;
    }


    @Override
    public PLCData saveModel(PLCData plcData) {
        log.info(plcData.toString());
        if(!plcData.equals(null))
        this.plcRepository.save(plcData);
        else
            log.info(null);
        return plcData;
    }

    @Override
    public PLCData getModelByID(Integer id) {
        PLCData plcData=plcRepository.findById(id).orElse(null);
        return plcData;
    }

//    @Override
//    public PLCData getModelBuID(int id) {
//
//        return plcRepository.findById(id).orElse(null);
//    }


@Transactional
    @Override
    public List<PLCData> getAllModel() {
        log.info("list get all");
        List<PLCData> list=plcRepository.findAll();
        list.stream().forEach(l->log.info("list: "+l.toString()));
        return list;
    }

    @Override
    public PLCData deleteModel(Integer id) {
        PLCData plcData=plcRepository.findById(id).orElse(null);
        plcRepository.delete(plcData);
        return plcData;
    }


//    @Override
//    public PLCData deleteModel(int id) {
//        PLCData plcData = plcRepository.findById(id).orElse(null);
//        plcRepository.delete(plcData);
//        return plcData;
//    }
}
