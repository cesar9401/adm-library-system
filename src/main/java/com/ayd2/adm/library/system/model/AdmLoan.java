package com.ayd2.adm.library.system.model;

import com.ayd2.adm.library.system.demo.LibConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "adm_loan")
@Getter
@Setter
public class AdmLoan {

    @Id
    @Column(name = "loan_id")
    private Long loanId;

    @Column(name = "creator_user_id")
    private Long creatorUserId;

    @JoinColumn(name = "student_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdmStudent student;

    @JoinColumn(name = "book_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdmBook book;

    @JsonFormat(pattern = LibConstant.DATETIME_FORMAT, shape = JsonFormat.Shape.STRING)
    @Column(name = "entry_date")
    private LocalDateTime entryDate;

    @JsonFormat(pattern = LibConstant.DATETIME_FORMAT, shape = JsonFormat.Shape.STRING)
    @Column(name = "expected_date_of_return")
    private LocalDateTime expectedDateOfReturn;

    @Column(name = "regular_loan_days")
    private Integer regularLoanDays;

    @Column(name = "loan_days_in_arrears")
    private Integer loanDaysInArrears;

    @Column(name = "total_loan_days")
    private Integer totalLoanDays;

    @Column(name = "payment_of_regular_days")
    private BigDecimal paymentOfRegularDays;

    @Column(name = "payment_of_days_in_arrears")
    private BigDecimal paymentOfDaysInArrears;

    @Column(name = "total_payment")
    private BigDecimal totalPayment;

    @JsonFormat(pattern = LibConstant.DATETIME_FORMAT, shape = JsonFormat.Shape.STRING)
    @Column(name = "return_date")
    private LocalDateTime returnDate;
}
