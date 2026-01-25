ALTER TABLE seat_reservation ADD COLUMN holder_session_id VARCHAR(100);
ALTER TABLE seat_reservation ADD COLUMN order_id BIGINT;

ALTER TABLE seat_reservation
    ADD CONSTRAINT fk_sr_order FOREIGN KEY (order_id) REFERENCES booking_order(id);

CREATE INDEX idx_sr_holder_session ON seat_reservation(holder_session_id);