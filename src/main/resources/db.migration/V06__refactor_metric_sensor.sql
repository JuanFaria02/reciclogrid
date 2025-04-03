ALTER TABLE metric ADD weight NUMERIC(5,2);

ALTER TABLE address ADD CONSTRAINT unique_lat_long UNIQUE (latitude, longitude);

ALTER TABLE sensor DROP CONSTRAINT collector_fk_sensor;
ALTER TABLE sensor ADD CONSTRAINT collector_fk_sensor FOREIGN KEY (collector_id) REFERENCES collector(id) ON DELETE CASCADE;

ALTER TABLE collector ADD code varchar(255);