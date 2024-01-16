
USE IWE
GO

CREATE TABLE tblconsignment (
  consignment_id bigint(20) NOT NULL AUTO_INCREMENT DEFAULT 0,
  cod_amount varchar(255) DEFAULT NULL,
  cod_collection_mode varchar(255) DEFAULT NULL,
  cod_favor_of varchar(255) DEFAULT NULL,
  consignment_type varchar(255) DEFAULT NULL,
  customer_code varchar(255) DEFAULT NULL,
  customer_reference_number varchar(255) DEFAULT NULL,
  declared_value double DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  dimension_unit varchar(255) DEFAULT NULL,
  height varchar(255) DEFAULT NULL,
  is_risk_surcharge_applicable bit(1) DEFAULT NULL,
  length varchar(255) DEFAULT NULL,
  load_type varchar(255) DEFAULT NULL,
  notes varchar(255) DEFAULT NULL,
  num_pieces bigint(20) DEFAULT NULL,
  service_type_id varchar(255) DEFAULT NULL,
  weight double DEFAULT NULL,
  weight_unit varchar(255) DEFAULT NULL,
  width varchar(255) DEFAULT NULL,
  PRIMARY KEY (consignment_id)
);
GO
