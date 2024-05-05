CREATE TABLE public.adm_category (
    category_id BIGINT NOT NULL,
    parent_category_id BIGINT,
    internal_id BIGINT NOT NULL,
    category_description VARCHAR(75) NOT NULL,
    category_value_1 VARCHAR(100),
    CONSTRAINT adm_category_pk PRIMARY KEY (category_id)
);

ALTER TABLE public.adm_category
ADD CONSTRAINT adm_category_adm_category_fk
FOREIGN KEY (parent_category_id)
REFERENCES public.adm_category (category_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE INDEX adm_category_index ON public.adm_category USING BTREE(internal_id);
