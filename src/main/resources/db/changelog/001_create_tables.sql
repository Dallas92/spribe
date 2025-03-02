-- Create tables
CREATE TABLE units (
    id                 BIGSERIAL PRIMARY KEY,
    number_of_rooms    INT            NOT NULL,
    accommodation_type VARCHAR(50)    NOT NULL,
    floor              INT            NOT NULL,
    cost               DECIMAL(10, 2) NOT NULL,
    description        TEXT           NOT NULL,
    created_at         TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP      NOT NULL DEFAULT NOW()
);

CREATE TABLE bookings (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT      NOT NULL,
    unit_id    BIGINT      NOT NULL,
    status     VARCHAR(50) NOT NULL,
    date_from  DATE        NOT NULL,
    date_to    DATE        NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_bookings_unit FOREIGN KEY (unit_id) REFERENCES units(id)
);

CREATE TABLE payments (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT         NOT NULL,
    unit_id    BIGINT         NOT NULL,
    booking_id BIGINT         NOT NULL,
    amount     NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP      NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_payments_unit FOREIGN KEY (unit_id) REFERENCES units(id),
    CONSTRAINT fk_payments_booking FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

-- Trigger to auto-update `updated_at`
CREATE FUNCTION update_timestamp() RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply trigger to tables
CREATE TRIGGER trigger_update_units
    BEFORE UPDATE ON units
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trigger_update_bookings
    BEFORE UPDATE ON bookings
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trigger_update_payments
    BEFORE UPDATE ON payments
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();
