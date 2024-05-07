
CREATE TABLE public.adm_book (
    book_id BIGINT NOT NULL,
    isbn VARCHAR(15) NOT NULL,
    title VARCHAR(250) NOT NULL,
    author VARCHAR(250) NOT NULL,
    publication_date DATE NOT NULL,
    editorial VARCHAR(200) NOT NULL,
    stock INTEGER NOT NULL,
    CONSTRAINT adm_book_pk PRIMARY KEY (book_id)
);

CREATE TABLE public.adm_career (
    career_id BIGINT NOT NULL,
    code VARCHAR(10) NOT NULL,
    name VARCHAR(150) NOT NULL,
    CONSTRAINT adm_career_pk PRIMARY KEY (career_id)
);

CREATE TABLE public.adm_student (
    student_id BIGINT NOT NULL,
    career_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    carnet VARCHAR(15) NOT NULL,
    first_name VARCHAR(150) NOT NULL,
    last_name VARCHAR(150) NOT NULL,
    birthday DATE NOT NULL,
    CONSTRAINT adm_student_pk PRIMARY KEY (student_id)
);

CREATE TABLE public.adm_loan (
    load_id BIGINT NOT NULL,
    creator_user_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    entry_date TIMESTAMP WITHOUT TIME ZONE DEFAULT '1900-01-01 00:00:00'::TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    expected_date_of_return TIMESTAMP WITHOUT TIME ZONE DEFAULT '1900-01-01 00:00:00'::TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    regular_loan_days INTEGER,
    loan_days_in_arrears INTEGER,
    total_loan_days INTEGER,
    payment_of_regular_days NUMERIC(20,6),
    payment_of_days_in_arrears NUMERIC(20,6),
    total_payment NUMERIC(20,6),
    return_date TIMESTAMP WITHOUT TIME ZONE DEFAULT '1900-01-01 00:00:00'::TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT adm_loan_pk PRIMARY KEY (load_id)
);

ALTER TABLE public.adm_student ADD CONSTRAINT adm_career_adm_student_fk
FOREIGN KEY (career_id)
REFERENCES public.adm_career (career_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.adm_student ADD CONSTRAINT adm_user_adm_student_fk
FOREIGN KEY (user_id)
REFERENCES public.adm_user (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.adm_loan ADD CONSTRAINT adm_user_adm_loan_fk
FOREIGN KEY (creator_user_id)
REFERENCES public.adm_user (user_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.adm_loan ADD CONSTRAINT adm_book_adm_loan_fk
FOREIGN KEY (book_id)
REFERENCES public.adm_book (book_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.adm_loan ADD CONSTRAINT adm_student_adm_loan_fk
FOREIGN KEY (student_id)
REFERENCES public.adm_student (student_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
