package com.upc.ejercicio.Ejercicio.service.impl;

import com.upc.ejercicio.Ejercicio.model.Car;
import com.upc.ejercicio.Ejercicio.repository.CarRepository;
import com.upc.ejercicio.Ejercicio.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Override
    public Car createCar(Car car) {
        return carRepository.save(car);
    }
}
