USE WMS
GO

CREATE OR ALTER PROCEDURE sp_update_thr_pickup_cs
    @warehouse varchar(10),
    @itemCode varchar(255) = NULL,
    @closingStockDateFrom DATETIME,
    @closingStockDateTo DATETIME
AS
BEGIN
    -- Validate the input parameters
    IF @warehouse IS NULL OR @warehouse = ''
    BEGIN
        RAISERROR('Invalid warehouse', 16, 1)
        RETURN
    END

    IF @closingStockDateFrom IS NULL OR @closingStockDateTo IS NULL
    BEGIN
        RAISERROR('Invalid date range', 16, 1)
        RETURN
    END
    
    IF @itemCode = '0'
	BEGIN
	SET @itemCode = NULL
	END

    -- Update data in tbltransactionhistoryresults
    UPDATE th SET th.pi_cs_qty = x.VALUE 
    FROM tbltransactionhistoryresults th 
    INNER JOIN 
        (SELECT SUM(PICK_CNF_QTY) VALUE,ITM_CODE itemCode 
        FROM tblpickupline 
        WHERE ITM_CODE IN 
            ((select itm_code from tblputawayline where wh_id=@warehouse and status_id in (20,22) and is_deleted=0 and 
            (itm_code = ISNULL(@itemCode,itm_code)))
            union
            (select itm_code from tblpickupline where wh_id=@warehouse and status_id in (50,59) and is_deleted=0 and 
            (itm_code = ISNULL(@itemCode,itm_code))) 
            union
            (select itm_code from tblinventorymovement where wh_id=@warehouse and MVT_TYP_ID =4 and SUB_MVT_TYP_ID=1 and is_deleted=0 and 
            (itm_code = ISNULL(@itemCode,itm_code)))) 
        AND STATUS_ID in (50,59) AND WH_ID=@warehouse AND IS_DELETED = 0 AND PICK_CTD_ON BETWEEN @closingStockDateFrom and @closingStockDateTo group by itm_code)x on x.itemCode=th.item_code
END

GO