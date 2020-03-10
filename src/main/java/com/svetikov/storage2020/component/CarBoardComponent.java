package com.svetikov.storage2020.component;

import com.svetikov.storage2020.models.BoardBox;
import com.svetikov.storage2020.models.PLCData;
import com.svetikov.storage2020.service.BorderService;
import com.svetikov.storage2020.service.ModelService;
import com.svetikov.storage2020.service.ServiceCarPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import javax.management.modelmbean.ModelMBean;

@Slf4j
public class CarBoardComponent {
    private final ServiceCarPosition carOneServiceCarPosition;

    private final ServiceCarPosition carTwoServiceCarPosition;
    private final ModelService<PLCData, Integer> plcService;
    private final BorderService boardService;

    @Value("${car1.namePLC}")
    private String namePLC1;
    @Value("${car2.namePLC}")
    private String namePLC2;

    @Autowired
    public CarBoardComponent(@Qualifier("car1") ServiceCarPosition carOneServiceCarPosition,
                             @Qualifier("car2") ServiceCarPosition carTwoServiceCarPosition,
                             @Qualifier("plc") ModelService plcService,
                             BorderService boardService) {
        this.carOneServiceCarPosition = carOneServiceCarPosition;
        this.carTwoServiceCarPosition = carTwoServiceCarPosition;
        this.plcService = plcService;
        this.boardService = boardService;
    }


    public void onInitData(int idPLC) {
        log.info("car1                      init ");
        PLCData plcDataL = plcService.getModelByID(idPLC);
        {
            if (plcDataL.getPlcName().equals("car1"))
                log.info("car1 init ");
            carOneServiceCarPosition.setPos();
        }

        if (plcDataL.getPlcName().equals("car2")) {
            log.info("car2 init ");
            carTwoServiceCarPosition.setPos();
        }
        if (plcDataL.getPlcName().equals("csl")) {
            boardService.setDataBoard_CSL();
        }
        if (plcDataL.getPlcName().equals("sl")) {
            boardService.setDataBoardSL();
        }
    }
}
