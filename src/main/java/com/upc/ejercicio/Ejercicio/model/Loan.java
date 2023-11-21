package com.upc.ejercicio.Ejercicio.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="code_user", length =10, nullable=false)
    private String codeUser;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(name = "devolution_date", nullable = false)
    private LocalDate devolutionDate;

    @Column(name="car_loan",nullable = false)
    private boolean carLoan;

    @ManyToOne
    @JoinColumn(name="car_id", nullable=false
            , foreignKey = @ForeignKey(name="FK_CAR_ID"))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Car car;

    @Column(name = "owner_uid", length = 50, nullable = false)
    private String ownerUid;
}

