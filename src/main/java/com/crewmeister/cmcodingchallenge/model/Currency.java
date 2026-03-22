package com.crewmeister.cmcodingchallenge.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity representing a currency with a unique code.
 */
@Entity
@Table(name = "currency")
@RequiredArgsConstructor
@Setter
@Getter
@ToString
public class Currency extends BaseEntity implements Serializable {

    /**
     * Constructor to create a currency with the given code.
     */
    public Currency(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /** Primary key for the currency entity. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_seq")
    @SequenceGenerator(name = "currency_seq", sequenceName = "currency_seq")
    private Long id;

    /** ISO code or identifier of the currency (e.g., EUR, USD). */
    @Column(name = "currency_code", nullable = false)
    private String currencyCode;
}
