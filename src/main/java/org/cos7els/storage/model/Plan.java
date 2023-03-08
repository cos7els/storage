package org.cos7els.storage.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@Entity
@Table(name = "plans", schema = "public")
public class Plan {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "available_space")
    private double availableSpace;
    @Column(name = "price_per_month")
    private double pricePerMonth;
    @Column(name = "price_per_year")
    private double pricePerYear;
    @JsonBackReference
    @OneToMany(mappedBy = "plan", fetch = FetchType.EAGER)
    private List<User> users;
}
