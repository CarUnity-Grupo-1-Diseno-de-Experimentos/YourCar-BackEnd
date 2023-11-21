package com.upc.ejercicio.Ejercicio.controller;


import com.upc.ejercicio.Ejercicio.exception.ResourceNotFoundException;
import com.upc.ejercicio.Ejercicio.exception.ValidationException;
import com.upc.ejercicio.Ejercicio.model.Car;
import com.upc.ejercicio.Ejercicio.repository.CarRepository;
import com.upc.ejercicio.Ejercicio.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car-unity/v1")
public class CarController {
    @Autowired
    private CarService carService;

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    //URL: http://localhost:8080/api/car-unity/v1/cars
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getAllCars() {
        return new ResponseEntity<List<Car>>(carRepository.findAll(), HttpStatus.OK);
    }

    //URL: http://localhost:8080/api/car-unity/v1/cars/available
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/cars/available")
    public ResponseEntity<List<Car>> getAvailableCars() {
        List<Car> availableCars = carRepository.findByAvailable(true);
        return new ResponseEntity<>(availableCars, HttpStatus.OK);
    }


    //URL: http://localhost:8080/api/car-unity/v1/cars
    //Method: POST
    @Transactional
    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        validateCar(car);
        //existsByTitleAndEditorial(car);
        return new ResponseEntity<Car>(carService.createCar(car), HttpStatus.CREATED);
    }

    //GetCarsByOwnerUid
    //URL: http://localhost:8080/api/car-unity/v1/cars/filterByOwnerUid
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/cars/filterByOwnerUid")
    public ResponseEntity<List<Car>> getAllCarsByOwnerUid(@RequestParam(name="ownerUid") String ownerUid) {
        return new ResponseEntity<List<Car>>(carRepository.findByOwnerUid(ownerUid), HttpStatus.OK);
    }

    //URL: http://localhost:8080/api/car-unity/v1/cars/filterByBrand
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/cars/filterByBrand")
    public ResponseEntity<List<Car>> getAllCarsByBrand(@RequestParam(name="brand") String brand) {
        return new ResponseEntity<List<Car>>(carRepository.findByBrand(brand), HttpStatus.OK);
    }

    //URL: http://localhost:8080/api/car-unity/v1/cars/{id}
    //Method: PUT
    @Transactional
    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable(value = "id") Long carId, @RequestBody Car updatedCar) {
        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el auto con id: " + carId));

        // Actualizar los campos necesarios del auto
        existingCar.setAvailable(updatedCar.isAvailable());  // Asigna el valor de available del nuevo objeto
        // Agrega otros campos si es necesario

        // Guardar el auto actualizado
        Car savedCar = carRepository.save(existingCar);

        return new ResponseEntity<>(savedCar, HttpStatus.OK);
    }

    private void validateCar(Car car){
        if(car.getBrand() == null || car.getBrand().isEmpty()){
            throw new ValidationException("La marca no debe estar vacia");
        }

        if(car.getBrand().length()>20){
            throw new ValidationException("La marca no debe tener mas de 14 caracteres");
        }

        if(car.getModel() ==null){
            throw new ValidationException("El modelo no debe estar vacio");
        }

        if(car.getModel().length()>20){
            throw new ValidationException("El modelo no debe tener mas de 20 caracteres");
        }

        if(car.getPhoto() ==null){
            throw new ValidationException("La foto no debe estar vacia");
        }

        if(car.getPhoto().length()>120){
            throw new ValidationException("El link de la foto no debe tener mas de 100 caracteres");
        }

        if(car.getMaxspeed() ==null){
            throw new ValidationException("La velocidad maxima no debe estar vacia");
        }

        if(car.getMaxspeed().length()>5){
            throw new ValidationException("La velocidad maxima no debe tener mas de 5 caracteres");
        }

        if(car.getFuelconsumption() ==null){
            throw new ValidationException("El consumo de combustible no debe estar vacio");
        }

        if(car.getFuelconsumption().length()>5){
            throw new ValidationException("El consumo de combustible no debe tener mas de 5 caracteres");
        }

        if(car.getDimension() ==null){
            throw new ValidationException("La dimension no debe estar vacia");
        }

        if(car.getDimension().length()>20){
            throw new ValidationException("La dimension no debe tener mas de 20 caracteres");
        }

        if(car.getWeight() ==null){
            throw new ValidationException("El peso no debe estar vacio");
        }

        if(car.getWeight().length()>5){
            throw new ValidationException("El peso no debe tener mas de 5 caracteres");
        }

        if(car.getYear() ==null){
            throw new ValidationException("El año no debe estar vacio");
        }

        if(car.getYear().length()>4){
            throw new ValidationException("El año no debe tener mas de 4 caracteres");
        }

        if(car.getEngine() ==null){
            throw new ValidationException("El motor no debe estar vacio");
        }

        if(car.getEngine().length()>14){
            throw new ValidationException("El motor no debe tener mas de 20 caracteres");
        }

        if(car.getTimeloan() ==null){
            throw new ValidationException("El tiempo de prestamo no debe estar vacio");
        }

        if(car.getTimeloan().length()>3){
            throw new ValidationException("El tiempo de prestamo no debe tener mas de 3 caracteres");
        }

        if(car.getPrice() ==null){
            throw new ValidationException("El precio no debe estar vacio");
        }

        if(car.getPrice().length()>10){
            throw new ValidationException("El precio no debe tener mas de 10 caracteres");
        }

        if(car.getUbication() ==null){
            throw new ValidationException("La ubicacion no debe estar vacia");
        }

        if(car.getUbication().length()>50){
            throw new ValidationException("La ubicacion no debe tener mas de 50 caracteres");
        }
    }

}
