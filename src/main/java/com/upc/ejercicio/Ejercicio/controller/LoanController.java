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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/car-unity/v1")
public class LoanController {

    private LoanRepository loanRepository;

    private CarRepository carRepository;

    public LoanController(LoanRepository loanRepository, CarRepository carRepository) {
        this.loanRepository = loanRepository;
        this.carRepository = carRepository;
    }

    //URL: http://localhost:8080/api/car-unity/v1/loans
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return new ResponseEntity<List<Loan>>(loanRepository.findAll(), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @GetMapping("/loans/filterByCodeUser")
    public ResponseEntity<List<Map<String, Object>>> getAllLoansByCodeUser(@RequestParam(name="codeUser") String codeUser) {
        List<Loan> loans = loanRepository.findByCodeUser(codeUser);

        // Mapear la lista de préstamos a objetos JSON que contengan la información del auto
        List<Map<String, Object>> loanResponses = loans.stream()
                .map(loan -> {
                    Map<String, Object> loanResponse = new HashMap<>();
                    loanResponse.put("id", loan.getId());
                    loanResponse.put("codeUser", loan.getCodeUser());
                    loanResponse.put("loanDate", loan.getLoanDate());
                    loanResponse.put("devolutionDate", loan.getDevolutionDate());
                    loanResponse.put("carLoan", loan.isCarLoan());
                    loanResponse.put("ownerUid", loan.getOwnerUid());

                    // Información del auto
                    Car car = loan.getCar();
                    Map<String, Object> carInfo = new HashMap<>();
                    carInfo.put("id", car.getId());
                    carInfo.put("brand", car.getBrand());
                    carInfo.put("model", car.getModel());
                    carInfo.put("year", car.getYear());
                    carInfo.put("photo", car.getPhoto());

                    loanResponse.put("car", carInfo);

                    return loanResponse;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(loanResponses, HttpStatus.OK);
    }


    //URL: http://localhost:8080/api/car-unity/v1/loans/filterByOwnerUid
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/loans/filterByOwnerUid")
    public ResponseEntity<List<Loan>> getAllLoansByOwnerUid(@RequestParam(name="codeOwner") String codeOwner) {
        return new ResponseEntity<List<Loan>>(loanRepository.findByOwnerUid(codeOwner), HttpStatus.OK);
    }

    //URL: http://localhost:8080/api/car-unity/v1/cars/1/loans
    //Method: POST
    @Transactional
    @PostMapping("/loans/{id}/loans")
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
        validateLoan(loan);
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
        //loan.setDevolutionDate(LocalDate.now());
        loan.setCarLoan(false);
        loanRepository.save(loan);

        return new ResponseEntity<>("Préstamo devuelto exitosamente", HttpStatus.OK);
    }

    private void validateLoan(Loan loan){
        if(loan.getCodeUser() == null || loan.getCodeUser().isEmpty()){
            throw new ValidationException("El codigo de estudiante no debe estar vacio");
        }

        if (loan.getCodeUser().length()>50){
            throw new ValidationException("El codigo de usuario no debe tener mas de 50 caracteres");
        }
    }

}
