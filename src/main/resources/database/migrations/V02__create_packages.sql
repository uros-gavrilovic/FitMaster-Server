CREATE TABLE IF NOT EXISTS package
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name     VARCHAR(255),
    duration INTEGER,
    price    DECIMAL,
    currency VARCHAR(3),

    CONSTRAINT pk_package PRIMARY KEY (id)
);
