package org.cos7els.storage.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "subscriptions", schema = "public")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscriptions_seq")
    @SequenceGenerator(name = "subscriptions_seq", sequenceName = "subscriptions_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "user_id")
    @GeneratedValue()
    private Long userId;
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