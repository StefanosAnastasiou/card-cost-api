package com.etraveli.cardcostapi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity Object for database
 */
@Entity
@Table(name = "Cost", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
})
@Getter
@Setter
public class Cost implements Serializable {
    private static final long serialVersionUID = -7319838756880821966L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name="country")
    private String country;

    @Column(name="cost")
    private Double cost;
}
