package com.svetikov.storage2020.component;


import com.svetikov.storage2020.models.CarOne;
import com.svetikov.storage2020.models.CarTwo;
import com.svetikov.storage2020.service.BorderService;
import com.svetikov.storage2020.service.ModelService;
import com.svetikov.storage2020.service.ServiceCarPosition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class ComponentPLCDATA {
    @Bean
    public PLCComponent plcComponent(ModelService modelService) {
        return new PLCComponent(modelService);
    }
    @Bean
    public CarBoardComponent carBoardComponent(@Qualifier("car1") ServiceCarPosition carOneServiceCarPosition,
                                               @Qualifier("car2") ServiceCarPosition carTwoServiceCarPosition,
                                               @Qualifier("plc") ModelService plcService,
                                               BorderService boardService){
        return new CarBoardComponent(carOneServiceCarPosition,carTwoServiceCarPosition,plcService,boardService);
    }
}
