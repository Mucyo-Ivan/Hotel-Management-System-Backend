DELIMITER //

CREATE TRIGGER after_booking_insert
AFTER INSERT ON bookings
FOR EACH ROW
BEGIN
    DECLARE total_amount DOUBLE;
    DECLARE days INT;
    
    -- Calculate number of days between check-in and check-out
    SET days = DATEDIFF(NEW.check_out, NEW.check_in);
    
    -- Get room price and calculate total amount
    SELECT price * days INTO total_amount
    FROM rooms
    WHERE id = NEW.room_id;
    
    -- Insert billing record
    INSERT INTO billing (booking_id, amount, generated_at)
    VALUES (NEW.id, total_amount, NOW());
END //

DELIMITER ; 