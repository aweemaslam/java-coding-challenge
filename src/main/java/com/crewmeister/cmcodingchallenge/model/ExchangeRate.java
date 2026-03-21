package com.crewmeister.cmcodingchallenge.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity representing an exchange rate for a specific currency and date.
 */
@Entity
@Table(
    name = "exchangerate",
    uniqueConstraints = @UniqueConstraint(columnNames = {"currency_id", "date"})
)
@RequiredArgsConstructor
@Setter
@Getter
@ToString
public class ExchangeRate extends BaseEntity implements Serializable {

    /** Primary key for the exchange rate entity. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_rate_seq")
    @SequenceGenerator(name = "exchange_rate_seq", sequenceName = "exchange_rate_seq")
    private Long id;

    /** The currency associated with this exchange rate. */
    @ManyToOne(optional = false)
    @JoinColumn(name="currency_id", nullable=false)
    private Currency currency;

    /** Date for which this exchange rate is valid. */
    @Column(name="date", nullable = false)
    private LocalDate date;

    /** Exchange rate value for the currency on the given date. */
    @Column(name="exchange_rate")
    private Double exchangeRate;
}