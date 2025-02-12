ALTER TABLE collector ADD company_id BIGINT not null;

ALTER TABLE collector ADD CONSTRAINT company_fk_collector FOREIGN KEY (company_id) REFERENCES company(id);
