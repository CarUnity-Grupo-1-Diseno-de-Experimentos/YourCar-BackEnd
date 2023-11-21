package com.upc.ejercicio.Ejercicio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="brand", length =20, nullable=false)
    private String brand;

    @Column(name="model", length =20, nullable=false)
    private String model;

    @Column(name="photo", length =120, nullable=false)
    private String photo;

    @Column(name="maxspeed", length =5, nullable=false)
    private String maxspeed;

    @Column(name="fuelconsumption", length =5, nullable=false)
    private String fuelconsumption;

    @Column(name="dimension", length =20, nullable=false)
    private String dimension;

    @Column(name="weight", length =5, nullable=false)
    private String weight;

    @Column(name="year", length =4, nullable=false)
    private String year;

    @Column(name="engine", length =14, nullable=false)
    private String engine;

    @Column(name="timeloan", length =3, nullable=false)
    private String timeloan;

    @Column(name="price", length =10, nullable=false)
    private String price;

    @Column(name="ubication", length =50, nullable=false)
    private String ubication;

    @Column(name="available", nullable=false)
    private boolean available;

    @Column(name = "owner_uid", length = 50, nullable = false)
    private String ownerUid;

}
