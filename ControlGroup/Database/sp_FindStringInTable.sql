USE CGCLARA
GO

CREATE OR ALTER PROCEDURE sp_FindStringInTable @stringToFind VARCHAR(max), @schema sysname, @table sysname 
AS

SET NOCOUNT ON

BEGIN TRY
   DECLARE @sqlCommand varchar(max) = 'SELECT * FROM [' + @schema + '].[' + @table + '] WHERE ' 
	   
   SELECT @sqlCommand = @sqlCommand + '[' + COLUMN_NAME + '] LIKE ''' + @stringToFind + ''' OR '
   FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_SCHEMA = @schema
   AND TABLE_NAME = @table 
   AND DATA_TYPE IN ('bigint', 'char','nchar','ntext','nvarchar','text','varchar')

   SET @sqlCommand = left(@sqlCommand,len(@sqlCommand)-3)
   EXEC (@sqlCommand)
   PRINT @sqlCommand
END TRY

BEGIN CATCH 
   PRINT 'There was an error. Check to make sure object exists.'
   PRINT error_message()
END CATCH 

EXEC sys.sp_MS_marksystemobject sp_FindStringInTable
GO

EXEC sp_FindStringInTable 10006, 'dbo', 'tblstorepartnerlisting' 
GO

select * 
from tblstorepartnerlisting 
where (
		(10006 in (co_owner_id_1, co_owner_id_2 , co_owner_id_3)) and
		(10007 in (co_owner_id_1, co_owner_id_2 , co_owner_id_3)) and
		(10008 in (co_owner_id_1, co_owner_id_2 , co_owner_id_3))
		)
and co_owner_id_4 is null and co_owner_id_5 is null

select * 
from tblstorepartnerlisting 
where (
		(10007 in (co_owner_id_1, co_owner_id_2)) and
		(10006 in (co_owner_id_1, co_owner_id_2)) -- and
		-- (10007in (co_owner_id_1, co_owner_id_2 , co_owner_id_3))
		)
and co_owner_id_3 is null and co_owner_id_4 is null and co_owner_id_5 is null

SELECT tl.CO_OWNER_ID_1 AS coOwnerId1, tl.CO_OWNER_ID_2 AS coOwnerId2, tl.CO_OWNER_ID_3 AS coOwnerId3, 
tl.CO_OWNER_ID_4 AS coOwnerId4, tl.CO_OWNER_ID_5 AS coOwnerId5, tl.CO_OWNER_NAME_1 AS coOwnerName1, 
tl.CO_OWNER_NAME_2 AS coOwnerName2, tl.CO_OWNER_NAME_3 AS coOwnerName3, tl.CO_OWNER_NAME_4 AS coOwnerName4, 
tl.CO_OWNER_NAME_5 AS coOwnerName5, tl.CO_OWNER_PER_1 AS coOwnerPercentage1, tl.CO_OWNER_PER_2 AS coOwnerPercentage2, 
tl.CO_OWNER_PER_3 AS coOwnerPercentage3, tl.CO_OWNER_PER_4 AS coOwnerPercentage4, tl.CO_OWNER_PER_5 AS coOwnerPercentage5, 
tl.STORE_NM AS storeName FROM tblstorepartnerlisting tl  
WHERE 1=1  AND ( :co_owner_id_1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2) AND    :co_owner_id_2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2) )  AND tl.CO_OWNER_ID_3 IS NULL AND tl.CO_OWNER_ID_4 IS NULL AND tl.CO_OWNER_ID_5 IS NULL

