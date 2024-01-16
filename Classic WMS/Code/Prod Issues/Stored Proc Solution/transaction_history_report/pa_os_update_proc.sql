USE WMS
GO

CREATE OR ALTER PROCEDURE sp_update_thr_putaway_os
    @warehouse varchar(10),
    @itemCode varchar(255) = NULL,
    @openingStockDateFrom DATETIME,
    @openingStockDateTo DATETIME
AS
BEGIN
    -- Validate the input parameters
    IF @warehouse IS NULL OR @warehouse = ''
    BEGIN
        RAISERROR('Invalid warehouse', 16, 1)
        RETURN
    END

    IF @openingStockDateFrom IS NULL OR @openingStockDateTo IS NULL
    BEGIN
        RAISERROR('Invalid date range', 16, 1)
        RETURN
    END
    
    IF @itemCode = '0'
	BEGIN
	SET @itemCode = NULL
	END

    -- Update data in tbltransactionhistoryresults
    UPDATE th SET th.pa_os_qty = x.VALUE 
    FROM tbltransactionhistoryresults th 
    INNER JOIN 
        (SELECT SUM(PA_CNF_QTY) VALUE,ITM_CODE itemCode 
        FROM tblputawayline 
        WHERE ITM_CODE IN 
            ((select itm_code from tblputawayline where wh_id=@warehouse and status_id in (20,22) and is_deleted=0 and 
            (itm_code = ISNULL(@itemCode,itm_code))) 
            union
            (select itm_code from tblpickupline where wh_id=@warehouse and status_id in (50,59) and is_deleted=0 and 
            (itm_code = ISNULL(@itemCode,itm_code))) 
            union
            (select itm_code from tblinventorymovement where wh_id=@warehouse and MVT_TYP_ID =4 and SUB_MVT_TYP_ID=1 and is_deleted=0 and 
            (itm_code = ISNULL(@itemCode,itm_code)))) 
        AND STATUS_ID = 20 AND WH_ID=@warehouse AND IS_DELETED = 0 AND PA_CTD_ON BETWEEN @openingStockDateFrom and @openingStockDateTo group by itm_code)x on x.itemCode=th.item_code
END

GO
