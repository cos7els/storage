package org.cos7els.storage.model.domain;

import lombok.Data;

import javax.persistence.*;

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