package com.svetikov.storage2020.service;

import com.svetikov.storage2020.models.Car;
import com.svetikov.storage2020.models.CarOne;
import si.trina.moka7.live.PLC;

public interface ServiceCarPosition<T extends  Car> {
    void setPos();

    void setPositionCar(int pos_x, int pos_z,boolean busy);

    T getPositionCar();



}
