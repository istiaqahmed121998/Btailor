-- Step 2: Create a function to update the `updated_at` column
CREATE OR REPLACE FUNCTION update_updated_at()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;  -- Update the `updated_at` column with the current timestamp
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Step 3: Create the trigger that will automatically update `updated_at` before any update on the users table
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at();