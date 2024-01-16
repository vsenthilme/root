USE WMS
GO
drop table tblmenuid;
Go
drop table tblmoduleid;
Go
drop table tblroleaccess;
Go
insert into tbluseraccess(c_id,lang_id,plant_id,usr_id,wh_id,usr_role_id,ctd_by,ctd_on,cur_decimal,date_for_id,is_deleted,mail_id,fst_nm,is_logged_in,lst_nm,password,status_id,time_zone,utd_by,utd_on,user_nm,usr_typ_id,reset_password) select c_id,lang_id,plant_id,usr_id,wh_id,usr_role_id,ctd_by,ctd_on,cur_decimal,date_for_id,is_deleted,mail_id,fst_nm,is_logged_in,lst_nm,password,status_id,time_zone,utd_by,utd_on,user_nm,usr_typ_id,reset_password) from tblusermanagement
Go