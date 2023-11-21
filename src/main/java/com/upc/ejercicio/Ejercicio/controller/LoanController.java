package com.upc.ejercicio.Ejercicio.controller;

import com.upc.ejercicio.Ejercicio.exception.ResourceNotFoundException;
import com.upc.ejercicio.Ejercicio.exception.ValidationException;
import com.upc.ejercicio.Ejercicio.model.Car;
import com.upc.ejercicio.Ejercicio.model.Loan;
import com.upc.ejercicio.Ejercicio.repository.CarRepository;
import com.upc.ejercicio.Ejercicio.repository.LoanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/car-unity/v1")
public class LoanController {

    private LoanRepository loanRepository;

    private CarRepository carRepository;

    public LoanController(LoanRepository loanRepository, CarRepository carRepository) {
        this.loanRepository = loanRepository;
        this.carRepository = carRepository;
    }


    //URL: http://localhost:8080/api/car-unity/v1/loans/filterByCodeUser
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/loans/filterByCodeUser")
    public ResponseEntity<List<Loan>> getAllLoansByCodeUser(@RequestParam(name="codeUser") String codeUser) {
        return new ResponseEntity<List<Loan>>(loanRepository.findByCodeUser(codeUser), HttpStatus.OK);
    }

    //URL: http://localhost:8080/api/car-unity/v1/cars/1/loans
    //Method: POST
    @Transactional
    @PostMapping("/cars/{id}/loans")
    public ResponseEntity<Loan> createLoan(@PathVariable(value = "id") Long carId, @RequestBody Loan loan) {

        // Dentro de LoanController.createLoan
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el auto con id: " + carId));

        if (!car.isAvailable()) {
            throw new ValidationException("El auto " + car.getBrand() + " no está disponible para alquiler en este momento.");
        }

        // Marcamos el auto como no disponible
        car.setAvailable(false);
        carRepository.save(car);

        loan.setCar(car);
        //existsLoanByCodeStudentAndBookLoan(loan, car);
        validateLoan(loan);
        loan.setLoanDate(LocalDate.now());
        loan.setDevolutionDate(LocalDate.now().plusDays(3));
        loan.setCarLoan(true);
        return new ResponseEntity<Loan>(loanRepository.save(loan), HttpStatus.CREATED);
    }

    //URL: http://localhost:8080/api/car-unity/v1/cars/{carId}/loans/{loanId}/return
    //Method: PUT
    @Transactional
    @PutMapping("/cars/{carId}/loans/{loanId}/return")
    public ResponseEntity<String> returnLoan(@PathVariable(value = "carId") Long carId, @PathVariable(value = "loanId") Long loanId) {
        // Buscar el préstamo y el auto asociado
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el préstamo con id: " + loanId));

        Car car = loan.getCar();

        // Marcar el auto como disponible
        car.setAvailable(true);
        carRepository.save(car);

        // Actualizar la información del préstamo
        loan.setDevolutionDate(LocalDate.now());
        loan.setCarLoan(false);
        loanRepository.save(loan);

        return new ResponseEntity<>("Préstamo devuelto exitosamente", HttpStatus.OK);
    }

    private void validateLoan(Loan loan){
        if(loan.getCodeUser() == null || loan.getCodeUser().isEmpty()){
            throw new ValidationException("El codigo de estudiante no debe estar vacio");
        }

        if (loan.getCodeUser().length()<10){
            throw new ValidationException("El codigo de estudiante debe tener 10 caracteres");
        }
    }

    private void existsLoanByCodeStudentAndBookLoan(Loan loan, Car car){
        if(loanRepository.existsByCodeUserAndCarAndCarLoan(loan.getCodeUser(), car, false)){
            throw new ValidationException("El alquiler del auto + " + car.getBrand() + " no es posible porque ya existe un alquiler pendiente para el usuario" + loan.getCodeUser());
        }
    }
}
