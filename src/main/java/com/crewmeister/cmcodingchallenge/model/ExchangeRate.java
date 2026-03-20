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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_rate_seq")
    @SequenceGenerator(name = "exchange_rate_seq", sequenceName = "exchange_rate_seq")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="currency_id", nullable=false)
    private Currency currency;

    @Column(name="date", nullable = false)
    private LocalDate date;
    
    @Column(name="exchange_rate")
    private Double exchangeRate;

}