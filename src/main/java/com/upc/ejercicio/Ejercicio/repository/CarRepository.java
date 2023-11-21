package com.upc.ejercicio.Ejercicio.repository;

import com.upc.ejercicio.Ejercicio.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByBrand(String brand);
    List<Car> findByAvailable(boolean available);
}
