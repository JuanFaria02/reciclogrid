CREATE TABLE address (
   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   street VARCHAR(255) NOT NULL,
   city VARCHAR(255) NOT NULL,
   state VARCHAR(255) NOT NULL,
   uf VARCHAR(2) NOT NULL,
   latitude VARCHAR(128) NOT NULL,
   latitude VARCHAR(128) NOT NULL,
   cep VARCHAR(9) NOT NULL,
   CONSTRAINT unique_lat_long UNIQUE (latitude, longitude)
);

CREATE TABLE company (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    corporate_name VARCHAR(255) NOT NULL,
    document_number VARCHAR(255) UNIQUE,
    email VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE employee (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(11),
    type VARCHAR(50) NOT NULL,
    password TEXT NOT NULL,
    document_number VARCHAR(255) UNIQUE.
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    position VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    company_id BIGINT,
    CONSTRAINT company_fk_employee FOREIGN KEY (company_id) REFERENCES company(id)
);

CREATE TABLE collector (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    address_id BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    CONSTRAINT company_fk_collector FOREIGN KEY (company_id) REFERENCES company(id),
    CONSTRAINT address_fk_collector FOREIGN KEY (address_id) REFERENCES address(id)
);

CREATE TABLE microcontroller (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    identifier_number VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    collector_id BIGINT NOT NULL,
    CONSTRAINT collector_fk_microcontroller FOREIGN KEY (collector_id) REFERENCES collector(id)
);

CREATE TABLE metric (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    distance BIGINT NOT NULL,
    percentage NUMERIC(5,2) NOT NULL,
    weight NUMERIC(5,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    collector_id BIGINT NOT NULL,
    CONSTRAINT collector_fk_sensor FOREIGN KEY (collector_id) REFERENCES collector(id)
);

