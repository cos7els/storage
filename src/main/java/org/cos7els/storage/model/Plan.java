package org.cos7els.storage.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "plans", schema = "public")
public class Plan {
    @Id
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "available_space")
    private Long availableSpace;
    @Column(name = "price_per_month")
    private double pricePerMonth;
    @Column(name = "price_per_year")
    private double pricePerYear;
}
