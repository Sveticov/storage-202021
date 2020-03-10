package com.svetikov.storage2020.service;

import com.svetikov.storage2020.component.PLCComponent;
import com.svetikov.storage2020.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import si.trina.moka7.live.PLC;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
@Slf4j
@Service
@Qualifier("car2")
public class CarTwoService implements ServiceCarPosition<CarTwo> {
    private CarTwo car;
    private PLC plc;
    private PLCComponent plcComponent;
    private final ModelService<PLCData, Integer> plcDataService;
    private final ModelService<BoardBox, Long> boardService;
    private final ModelService<SettingsST,Integer> settingsSTService;
    ExecutorService executorService;// = Executors.newFixedThreadPool(3);

    @Autowired
    public CarTwoService(PLCComponent plcComponent,
                         @Qualifier("plc") ModelService plcDataService,
                         @Qualifier("board") ModelService boardService,
                         @Qualifier("settingsST") ModelService settingsSTService) {
        this.car = new CarTwo();
        this.plcComponent = plcComponent;
//        plc = plcComponent.getPlc();
        this.plcDataService = plcDataService;
        this.boardService = boardService;
        this.settingsSTService=settingsSTService;
        executorService = Executors.newFixedThreadPool(3);
    }

    @Value("${car2.adr.xpos}")
    private int xpos;
    @Value("${car2.adr.zpos}")
    private int zpos;
    @Value("${car2.adr.buzyH}")
    private int busyH;
    @Value("${car2.adr.buzyL}")
    private int busyL;
    @Value("${car.ip_address.plc}")
    private String ip_addressPLC;
    @Value("${car2.namePLC}")
    private String namePLC;

    @Override
    public void setPos() {

        for (Map.Entry<String, PLC> plcEntry : plcComponent.plcMap().entrySet()) {
            if (plcEntry.getKey().equals(namePLC)) {
                plc = plcEntry.getValue();
            }
        }
        PLCData plcData = plcDataService.getAllModel().stream()
                .filter(p -> p.getPlcName().equals("car2"))
                .findFirst()
                .get();
        System.out.println(plcData);

        List<PLCDbData> plcDbDataINT = plcData.getPlcDbData().stream()
                .filter(d -> d.getTypeDB().equals("INT"))
                .sorted(Comparator.comparingInt(PLCDbData::getDbByte))
                .collect(Collectors.toList());

        List<PLCDbData> plcDbDataBOOL = plcData.getPlcDbData().stream()
                .filter(d -> d.getTypeDB().equals("BOOL"))
                .sorted(Comparator.comparingInt(PLCDbData::getDbByte))
                .collect(Collectors.toList());
        // TODO: 2/6/2020 Runnable car 2
        Runnable taskCar2 = () -> {
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
        Runnable taskCar2Board = () -> {
            CarPUT_GET_BOARD();
        };

        executorService.execute(taskCar2);
        executorService.execute(taskCar2Board);


    }

    @Override
    public void setPositionCar(int pos_x, int pos_z, boolean busy) {
        car.setPositionX(pos_x);
        car.setPositionZ(pos_z);
        car.setBusy(busy);
    }

    @Override
    public CarTwo getPositionCar() {
        return car;
    }


    private void CarPUT_GET_BOARD() {
        boolean status = false;
        while (true) {
            if (car.isBusy() != true && status == true) {
                boardService.saveModel(new BoardBox(
                        (int)((car.getPositionX()-100000)/111.67)+100,
                        (int)(( car.getPositionZ()-143570)/(-90.698))+100,
                        1250, 1600, 16));
                status = false;
            }
            if (car.isBusy() == true) status = true;

            //
            // TODO: 2/13/2020  car2 delete box
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
