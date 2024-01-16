USE WMS
GO

CREATE OR ALTER PROCEDURE sp_update_thr_inv_os
    @warehouse varchar(10),
    @itemCode varchar(255) = NULL
AS
BEGIN
    -- Validate the input parameters
    IF @warehouse IS NULL OR @warehouse = ''
    BEGIN
        RAISERROR('Invalid warehouse', 16, 1)
        RETURN
    END
    
    IF @itemCode = '0'
	BEGIN
	SET @itemCode = NULL
	END

    -- Update data in tbltransactionhistoryresults
    UPDATE th SET th.is_os_qty = x.value 
    FROM tbltransactionhistoryresults th 
    INNER JOIN 
        (SELECT (SUM(COALESCE(INV_QTY,0)) + SUM(COALESCE(ALLOC_QTY,0))) value,ITM_CODE itemCode 
        FROM tblinventorystock 
        WHERE ITM_CODE IN 
            ((SELECT ITM_CODE FROM TBLPUTAWAYLINE WHERE WH_ID=@warehouse AND STATUS_ID IN (20,22) AND IS_DELETED=0 AND 
            (itm_code = ISNULL(@itemCode,itm_code))) 
            union 
            (SELECT ITM_CODE FROM TBLPICKUPLINE WHERE WH_ID=@warehouse AND STATUS_ID IN (50,59) AND IS_DELETED=0 AND 
            (itm_code = ISNULL(@itemCode,itm_code))) 
            union 
            (SELECT ITM_CODE FROM TBLINVENTORYMOVEMENT WHERE WH_ID=@warehouse AND MVT_TYP_ID =4 AND SUB_MVT_TYP_ID=1 AND IS_DELETED=0 AND 
            (itm_code = ISNULL(@itemCode,itm_code)))) 
        AND WH_ID=@warehouse AND BIN_CL_ID in (1,4) GROUP BY ITM_CODE) X ON X.ITEMCODE=TH.ITEM_CODE
END

GO
