DELIMITER //

-- Drop existing trigger if it exists
DROP TRIGGER IF EXISTS after_booking_insert //

-- Create the trigger
CREATE TRIGGER after_booking_insert
AFTER INSERT ON bookings
FOR EACH ROW
BEGIN
    DECLARE total_amount DECIMAL(10,2);
    DECLARE nights INT;
    DECLARE room_price DECIMAL(10,2);
    
    -- Calculate number of nights
    SET nights = DATEDIFF(NEW.check_out_date, NEW.check_in_date);
    
    -- Get room price
    SELECT price INTO room_price
    FROM rooms
    WHERE id = NEW.room_id;
    
    -- Calculate total amount
    SET total_amount = room_price * nights;
    
    -- Insert billing record
    INSERT INTO billings (booking_id, amount, generated_at)
    VALUES (NEW.id, total_amount, NOW());
END //

DELIMITER ; 