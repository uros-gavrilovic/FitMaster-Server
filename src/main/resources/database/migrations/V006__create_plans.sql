CREATE TABLE plans
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    member_id  BIGINT,
    trainer_id BIGINT,
    starts_at  TIMESTAMP,
    ends_at    TIMESTAMP,
    comment    VARCHAR(512),
    completed  BOOLEAN,

    CONSTRAINT pk_plans PRIMARY KEY (id),
    CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES members (id),
    CONSTRAINT fk_trainer_id FOREIGN KEY (trainer_id) REFERENCES trainers (id)
)