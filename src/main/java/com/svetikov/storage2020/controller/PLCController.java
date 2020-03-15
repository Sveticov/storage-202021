package com.svetikov.storage2020.controller;

import com.svetikov.storage2020.component.CarBoardComponent;
import com.svetikov.storage2020.component.PLCComponent;
import com.svetikov.storage2020.models.*;
import com.svetikov.storage2020.service.ModelService;
import com.svetikov.storage2020.service.ServiceCarPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.trina.moka7.live.PLC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
@RestController

@RequestMapping("/app")
public class PLCController {

    private final ModelService<PLCData, Integer> plcService;

    private final ModelService<BoardBox, Long> boardService;

    private final PLCComponent plcComponent;
    private final CarBoardComponent carBoardComponent;

    private final ModelService<PLCDbData, Integer> plcDbService;
    private final ServiceCarPosition<CarOne> carOneServiceCarPosition;
    private final ServiceCarPosition<CarTwo> carTwoServiceCarPosition;

    private List<PLCDbData> plcDbDataSet;
    private  PLCData plcData1;
    private PLCStatus plcStatus;
    private List<PLCStatus> plcStatusList;


    @Autowired
    public PLCController(@Qualifier("plc") ModelService<PLCData, Integer> plcRepository,
                         @Qualifier("board") ModelService<BoardBox, Long> boardRepository,
                         PLCComponent plcComponent,
                         CarBoardComponent carBoardComponent,
                         @Qualifier("db") ModelService plcDbDataIntegerModelService,
                         @Qualifier("car1") ServiceCarPosition carOneServiceCarPosition,
                         @Qualifier("car2") ServiceCarPosition carTwoServiceCarPosition) {
        this.plcService = plcRepository;
        this.boardService = boardRepository;
        this.plcComponent = plcComponent;
        this.carBoardComponent=carBoardComponent;
        this.plcDbService = plcDbDataIntegerModelService;
        this.carOneServiceCarPosition=carOneServiceCarPosition;
        this.carTwoServiceCarPosition=carTwoServiceCarPosition;
        plcStatus=new PLCStatus();
        plcStatusList=new ArrayList<>();

    }

//    @GetMapping("/1")
//    public List<PLCData> getA() {
//        return plcRepository.getAllModel();
//    }
//
//    @GetMapping("/2")
//    public void getB() throws Exception {
//        plcComponent.onInitDBAreaPLC();
//    }
//
//    @GetMapping("/3")
//    public List<BoardBox> box() {
//
//        return boardRepository.getAllModel();
//    }
//    @GetMapping("/5")
//    public ResponseEntity< PLCData> getOne(){
//        PLCData plc=new PLCData("test","1",9,1,2,3,4,5);
//        return new ResponseEntity<>(plc,HttpStatus.OK);
//    }

    @PostMapping("/plc/newplc")
    public ResponseEntity<List<PLCData>> newPLCData(@RequestBody PLCData plcData) {

        log.info("plcData " + plcData.toString());

        if (plcData.getAdrIP().equals(null) & plcData.getDbRead() == 0)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        log.info("!plcService.getAllModel().isEmpty() " + plcService.getAllModel().isEmpty());
        if (!plcService.getAllModel().isEmpty()) {


            plcData1 = plcService.getAllModel().stream()
                    .filter(plc -> plc.getPlcName().equals(plcData.getPlcName()))
                    .findFirst()
                    .orElse(new PLCData("", "", -1, -1, -1,
                            -1, -1, -1, null));



            if (plcData1.getPlcName().equals(plcData.getPlcName())) {
                plcService.deleteModel(plcData1.getId());
            }
        }

        if (!plcDbService.getAllModel().isEmpty()) {

            plcDbDataSet = plcDbService.getAllModel().stream()
                    .filter(db -> db.getPlcName().equals(plcData.getPlcName()))
                    .collect(Collectors.toList());

            if (!plcDbDataSet.isEmpty()) {
                log.info("!plcDbDataSet.isEmpty() " + plcDbDataSet.isEmpty());
                plcData.setPlcDbData(plcDbDataSet);

            } else {
                plcData.setPlcDbData(null);
            }
        }
        plcService.saveModel(plcData);

        return new ResponseEntity<>(plcService.getAllModel(), HttpStatus.OK);
    }

    @GetMapping("/plc/all")
    public ResponseEntity<List<PLCData>> allPLCs() {
        return new ResponseEntity<>(plcService.getAllModel(), HttpStatus.OK);
    }

    @GetMapping("/plc/delete/{id}")
    public ResponseEntity<List<PLCData>> deletePLC(@PathVariable("id") int id) {
        PLCData plcData = plcService.getModelByID(id);
        plcService.deleteModel(id);
        return new ResponseEntity<>(plcService.getAllModel(), HttpStatus.OK);
    }

    // TODO: 2/6/2020 connect to plc 
    @GetMapping("/plc/connect/{id}")
    public ResponseEntity<Boolean> plcConnect(@PathVariable("id") int id) throws Exception {
        log.info("connect plc");
        plcComponent.onInitPLC(id);
        carBoardComponent.onInitData(id);



        return new ResponseEntity<>(plcComponent.getPlc().connected, HttpStatus.OK);
    }


    @GetMapping("/plc/status/connect")
    public ResponseEntity<List<PLCStatus>> statusPLC(){

       for (Map.Entry<String,PLC> plcEntry:plcComponent.statusPLC().entrySet()){
           plcStatus.setNamePLCStatus(plcEntry.getValue().PLCName);
           plcStatus.setStatusPLCStatus(plcEntry.getValue().connected);
           plcStatusList.add(plcStatus);
       }
       if(plcStatusList.isEmpty()){
           return ResponseEntity.ok(null);
       }
       else
        return ResponseEntity.ok(plcStatusList);
    }

}
