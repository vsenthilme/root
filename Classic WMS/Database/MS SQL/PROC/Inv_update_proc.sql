USE WMS
GO

CREATE OR ALTER PROCEDURE inv_update_proc 
	@warehouseId nvarchar(5), 
	@packBarcodes nvarchar(20), 
	@itmCode nvarchar(20), 
	@storageBin nvarchar(20),
	@pl_allocatedQty int,
	@pl_confirmQty int
AS
	DECLARE @NEW_INV_QTY int, @NEW_ALLOC_QTY int, @INV_QTY int, @ALLOC_QTY int

	SELECT @INV_QTY = INV_QTY, @ALLOC_QTY = ALLOC_QTY
	FROM tblinventory
	WHERE WH_ID = @warehouseId AND ITM_CODE = @itmcode 
	AND PACK_BARCODE = @packBarcodes AND ST_BIN = @storageBin

	SET @NEW_INV_QTY = (@INV_QTY + @pl_allocatedQty) - @pl_confirmQty
	SET @NEW_ALLOC_QTY = (@ALLOC_QTY - @pl_allocatedQty)

	IF @NEW_INV_QTY < 0
		SET @NEW_INV_QTY = 0

	IF @NEW_ALLOC_QTY < 0
		SET @NEW_ALLOC_QTY = 0

	UPDATE tblinventory
	SET INV_QTY = @NEW_INV_QTY, ALLOC_QTY = @ALLOC_QTY
	WHERE WH_ID = @warehouseId AND ITM_CODE = @itmCode 
	AND PACK_BARCODE = @packBarcodes AND ST_BIN = @storageBin
GO
