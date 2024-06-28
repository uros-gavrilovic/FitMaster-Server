CREATE TABLE IF NOT EXISTS membership
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    member_id  BIGINT,
    package_id BIGINT,
    start_date DATE,
    end_date   DATE,

    CONSTRAINT pk_memberships PRIMARY KEY (id),
    CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    CONSTRAINT fk_package_id FOREIGN KEY (package_id) REFERENCES package (id) ON DELETE CASCADE
);
