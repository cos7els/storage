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
@Table(name = "authorities", schema = "public")
public class Authority {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authorities_seq")
    @SequenceGenerator(name = "authorities_seq", sequenceName = "authorities_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "name")
    private String name;
}