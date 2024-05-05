ALTER TABLE public.adm_permission ADD COLUMN parent_permission_id BIGINT;

ALTER TABLE public.adm_permission
ADD CONSTRAINT adm_permission_adm_permission_fk
FOREIGN KEY (parent_permission_id)
REFERENCES adm_permission (permission_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
