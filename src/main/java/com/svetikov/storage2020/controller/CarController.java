package com.svetikov.storage2020.controller;

import com.svetikov.storage2020.models.CarOne;
import com.svetikov.storage2020.models.CarTwo;
import com.svetikov.storage2020.service.ServiceCarPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/app/car")
public class CarController {

    private final ServiceCarPosition carOneServiceCarPosition;

    private final ServiceCarPosition carTwoServiceCarPosition;

    @Autowired
    public CarController(@Qualifier("car1") ServiceCarPosition carOneServiceCarPosition,
                         @Qualifier("car2") ServiceCarPosition carTwoServiceCarPosition) {
        this.carOneServiceCarPosition = carOneServiceCarPosition;
        this.carTwoServiceCarPosition = carTwoServiceCarPosition;
    }

    @GetMapping("/test1/{x}/{z}/{busy}")
    public void setPOS_TEST(@PathVariable("x")int x,@PathVariable("z")int z,@PathVariable("busy") boolean busy){
        carOneServiceCarPosition.setPositionCar(x,z,busy);
    }
    @GetMapping("/test2/{x}/{z}/{busy}")
    public void setPOS2_TEST(@PathVariable("x")int x,@PathVariable("z")int z,@PathVariable("busy") boolean busy){
        carTwoServiceCarPosition.setPositionCar(x,z,busy);
    }

    @GetMapping("/car1")
    public ResponseEntity<CarOne> carPosition1() {
      //  carOneServiceCarPosition.setPos();
        return new ResponseEntity(carOneServiceCarPosition.getPositionCar(), HttpStatus.OK);
    }


    @GetMapping("/car2")
    public ResponseEntity<CarTwo> carPosition2() {
      //  carTwoServiceCarPosition.setPos();
        return new ResponseEntity(carTwoServiceCarPosition.getPositionCar(), HttpStatus.OK);
    }
}
