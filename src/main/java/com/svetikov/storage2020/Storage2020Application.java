package com.svetikov.storage2020;

import com.svetikov.storage2020.component.PLCComponent;
import com.svetikov.storage2020.models.*;
import com.svetikov.storage2020.service.ModelService;
import com.svetikov.storage2020.service.ServiceCarPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Storage2020Application implements CommandLineRunner {
    @Qualifier("plc")
    private final ModelService<PLCData,Integer> modelService;
    @Qualifier("board")
    private final ModelService<BoardBox,Long> modelBoxService;
    private final PLCComponent plcComponent;

    @Autowired
    @Qualifier("car1")
    ServiceCarPosition serviceCarPosition1;
    @Autowired
    @Qualifier("car2")
    ServiceCarPosition serviceCarPosition2;

    @Autowired
    public Storage2020Application(@Qualifier("plc") ModelService modelService,@Qualifier("board")ModelService modeBoxService, PLCComponent plcComponent) {
        this.modelService = modelService;
        this.modelBoxService=modeBoxService;
        this.plcComponent = plcComponent;
    }


    public static void main(String[] args) {
        SpringApplication.run(Storage2020Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("start test");
//        PLCData plcData = new PLCData(
//                "192.168.0.190",
//                8,
//                32,
//                1,
//                0,
//                0,
//                2
//        );
//
//
//
//        modelService.saveModel(plcData);
//        Thread.sleep(3000);
//        plcComponent.onInitPLC();
//        plcComponent.onInitDBAreaPLC();


//        BoardBox boardBox=new BoardBox(1,1,1,1,1);
//modelBoxService.saveModel(boardBox);


serviceCarPosition1.setPositionCar(120,340,false);
serviceCarPosition2.setPositionCar(700,600,false);


    }
}
