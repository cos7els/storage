package org.cos7els.storage.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "subscriptions", schema = "public")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscriptions_seq")
    @SequenceGenerator(name = "subscriptions_seq", sequenceName = "subscriptions_id_seq", allocationSize = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    private Plan plan;
    @Column(name = "issued_date")
    private LocalDate issuedDate;
    @Column(name = "expired_date")
    private LocalDate expiredDate;
    @Column(name = "is_active")
    private Boolean isActive;
}
