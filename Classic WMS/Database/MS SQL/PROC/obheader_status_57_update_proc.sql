USE WMS
GO

-- String warehouseId, String preOutboundNo, String refDocNumber, String partnerCode
CREATE OR ALTER PROCEDURE obheader_status_57_update_proc 
	@warehouseId nvarchar(5), 
	@refDocNumber nvarchar(20),
	@preOutboundNo nvarchar(20), 
	@partnerCode nvarchar(20),
	@updatedBy nvarchar(20)
AS
	DECLARE @ROW_COUNT int, @ROW_COUNT_STATUS_ID_57 int, @IS_STATUS_ID_57 int = 0

	SELECT @ROW_COUNT = COUNT(REF_DOC_NO) FROM tbloutboundline 
	WHERE WH_ID = @warehouseId AND 
	REF_DOC_NO = @refDocNumber AND
	PRE_OB_NO = @preOutboundNo AND
	PARTNER_CODE = @partnerCode 
	GROUP BY REF_DOC_NO

	SELECT @ROW_COUNT_STATUS_ID_57 = COUNT(REF_DOC_NO) FROM tbloutboundline 
	WHERE WH_ID = @warehouseId AND 
	REF_DOC_NO = @refDocNumber AND
	PRE_OB_NO = @preOutboundNo AND
	PARTNER_CODE = @partnerCode AND
	STATUS_ID = 57 
	GROUP BY REF_DOC_NO

	IF @ROW_COUNT = @ROW_COUNT_STATUS_ID_57
		SET @IS_STATUS_ID_57 = 1

	IF @IS_STATUS_ID_57 = 1
		UPDATE tbloutboundheader
			SET STATUS_ID = 57, DLV_UTD_BY = @updatedBy
			WHERE WH_ID = @warehouseId AND 
			REF_DOC_NO = @refDocNumber AND
			PRE_OB_NO = @preOutboundNo AND
			PARTNER_CODE = @partnerCode
GO

