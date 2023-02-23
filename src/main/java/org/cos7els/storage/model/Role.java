package org.cos7els.storage.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    private String name;
}
