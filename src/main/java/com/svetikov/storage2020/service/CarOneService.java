package com.svetikov.storage2020.service;

import com.svetikov.storage2020.component.PLCComponent;
import com.svetikov.storage2020.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import si.trina.moka7.live.PLC;


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@Qualifier("car1")
public class CarOneService implements ServiceCarPosition<CarOne> {
    private CarOne car;
    private PLC plc;
    private PLCComponent plcComponent;
    private final ModelService<PLCData, Integer> plcDataService;
    private final ModelService<BoardBox, Long> boardService;
    private final ModelService<SettingsST,Integer> settingsSTService;
    ExecutorService executorService;// = Executors.newFixedThreadPool(3);


    @Autowired
    public CarOneService(PLCComponent plcComponent,
                         @Qualifier("plc") ModelService plcDataService,
                         @Qualifier("board") ModelService boardService,
                         @Qualifier("settingsST") ModelService settingsSTService) {
        this.car = new CarOne();
        this.plcComponent = plcComponent;
        // plc = plcComponent.getPlc();
        this.plcDataService = plcDataService;
        this.boardService = boardService;
        this.settingsSTService=settingsSTService;
        executorService = Executors.newFixedThreadPool(3);

    }

    @Value("${car1.adr.xpos}")
    private int xpos;
    @Value("${car1.adr.zpos}")
    private int zpos;
    @Value("${car1.adr.buzyH}")
    private int busyH;
    @Value("${car1.adr.buzyL}")
    private int busyL;
    @Value("${car.ip_address.plc}")
    private String ip_addressPLC;
    @Value("${car1.namePLC}")
    private String namePLC;


    @Override
    public void setPos() {

        for (Map.Entry<String, PLC> plcEntry : plcComponent.plcMap().entrySet()) {
            if (plcEntry.getKey().equals(namePLC)) {
                plc = plcEntry.getValue();
            }
        }
//
        PLCData plcData = plcDataService.getAllModel().stream()
                .filter(p -> p.getPlcName().equals("car1"))
                .findFirst()
                .get();
//
//
        List<PLCDbData> plcDbDataINT = plcData.getPlcDbData().stream()
                .filter(d -> d.getTypeDB().equals("INT"))
                .sorted(Comparator.comparingInt(PLCDbData::getDbByte)) // TODO: 2/6/2020 sorted from byte
                .collect(Collectors.toList());

        List<PLCDbData> plcDbDataBOOL = plcData.getPlcDbData().stream()
                .filter(d -> d.getTypeDB().equals("BOOL"))
                .sorted(Comparator.comparingInt(PLCDbData::getDbByte))
                .collect(Collectors.toList());


//
        // TODO: 2/6/2020 Runnamble Car 1
        Runnable taskCar1 = () -> {
            while (true) {
                try {
                    car.setPositionZ(plc.getDInt(true, plcDbDataINT.get(1).getDbByte()));
                    car.setPositionX(plc.getDInt(true, plcDbDataINT.get(0).getDbByte()));
                    car.setBusy(plc.getBool(true, plcDbDataBOOL.get(0).getDbByte(), plcDbDataBOOL.get(0).getDbBit()));
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        Runnable taskCar1Board = () -> {
            CarPUT_GET_BOARD();
        };


        executorService.execute(taskCar1);
        executorService.execute(taskCar1Board);


    }

    @Override
    public void setPositionCar(int pos_x, int pos_z,boolean busy) {
        car.setPositionX(pos_x);
        car.setPositionZ(pos_z);
        car.setBusy(busy);
    }

    @Override
    public CarOne getPositionCar() {
        return car;
    }


    private void CarPUT_GET_BOARD() {


        boolean status = false;
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (car.isBusy() != true && status == true) {
                boardService.saveModel(new BoardBox(
                        (int)((car.getPositionX()-100000)/111.67)+100,
                        (int)(( car.getPositionZ()-143570)/(-90.698))+100,
                        1250, 1600, 16));
                status = false;
            }
            if (car.isBusy() == true) status = true;
            //____________________________//------------------------------->
// TODO: 2/7/2020 delete board
            int x = (int)((car.getPositionX()-100000)/111.67)+100;
            int z = (int)(( car.getPositionZ()-143570)/(-90.698))+100;
            int offset_x=settingsSTService.getAllModel().stream().findFirst().get().getCarBusyOffsetX();
            int offset_z=settingsSTService.getAllModel().stream().findFirst().get().getCarBusyOffsetZ();

            if (car.isBusy() == true) {
                for (BoardBox board : boardService.getAllModel()) {
                    boolean x_status_m = board.getPositionXBox() - x >= -offset_x;
                    boolean x_status_p = board.getPositionXBox() - x <= offset_x;
                    boolean z_status_m = board.getPositionYBox() - z >= -offset_z;
                    boolean z_status_p = board.getPositionYBox() - z <= offset_z;
                    if ((x_status_m && x_status_p) && (z_status_m && z_status_p)) {
                        log.error(board.toString());
                        log.error(board.toString());
                        boardService.deleteModel(board.getIdBox());
                    }

                }
            }


        }

    }
}
