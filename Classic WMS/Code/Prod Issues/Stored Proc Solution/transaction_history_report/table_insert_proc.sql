USE WMS
GO

CREATE OR ALTER PROCEDURE sp_insert_thr
    @warehouseId varchar(10),
    @itemCode varchar(255) = NULL
AS
BEGIN
    -- Validate the input parameters
    IF @warehouseId IS NULL OR @warehouseId = ''
    BEGIN
        RAISERROR('Invalid warehouse id', 16, 1)
        RETURN
    END
    
    IF @itemCode = '0'
	BEGIN
	SET @itemCode = NULL
	END

    -- Insert data into tbltransactionhistoryresults
    INSERT INTO tbltransactionhistoryresults(item_code,description,warehouse_id) 
        SELECT itm_code,text description,wh_id warehouseId 
        FROM tblimbasicdata1 
        WHERE wh_id = @warehouseId 
        AND is_deleted=0 
        AND itm_code = ISNULL(@itemCode,itm_code)
END

GO
