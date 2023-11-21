package com.upc.ejercicio.Ejercicio.repository;

import com.upc.ejercicio.Ejercicio.model.Car;
import com.upc.ejercicio.Ejercicio.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long>{

    boolean existsByCodeUserAndCarAndCarLoan(String codeUser, Car car, boolean carLoan);

    //boolean existsByCodeUser

    List<Loan> findByCodeUser(String codeUser);

    List<Loan> findByOwnerUid(String codeOwner);
}
