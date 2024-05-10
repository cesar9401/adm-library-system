package com.ayd2.adm.library.system.model;

import com.ayd2.adm.library.system.util.LibConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "adm_book")
@Getter
@Setter
public class AdmBook {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookIdGenerator")
    @SequenceGenerator(name = "bookIdGenerator", sequenceName = "SEQ_ADM_BOOK", initialValue = 15000, allocationSize = 1)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LibConstant.DATE_FORMAT)
    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "editorial")
    private String editorial;

    @Column(name = "stock")
    private Integer stock;
}
