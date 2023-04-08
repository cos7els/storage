package org.cos7els.storage.model.domain;

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
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plans_seq")
    @SequenceGenerator(name = "plans_seq", sequenceName = "plans_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "available_space")
    private Long availableSpace;
    @Column(name = "monthly_price")
    private Double monthlyPrice;
    @Column(name = "yearly_price")
    private Double yearlyPrice;
    @Column(name = "is_active")
    private Boolean isActive;
}