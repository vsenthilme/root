USE WMS
GO
update tblpreinboundheader set ref_field_10 = (select status_text from tblstatusid where tblstatusid.lang_id=tblpreinboundheader.lang_id and tblstatusid.status_id=tblpreinboundheader.status_id )
GO
update tblstagingheader set ref_field_10 = (select status_text from tblstatusid where tblstatusid.lang_id=tblstagingheader.lang_id and tblstatusid.status_id=tblstagingheader.status_id)
GO
update tblputawayheader set ref_field_10 = (select status_text from tblstatusid where tblstatusid.lang_id=tblputawayheader.lang_id and tblstatusid.status_id=tblputawayheader.status_id )
GO
update tblinboundheader set ref_field_10 = (select status_text from tblstatusid where tblstatusid.lang_id=tblinboundheader.lang_id and tblstatusid.status_id=tblinboundheader.status_id )
GO
update tblqualityheader set ref_field_10 = (select status_text from tblstatusid where tblstatusid.lang_id=tblqualityheader.lang_id and tblstatusid.status_id=tblqualityheader.status_id )
GO
update tbloutboundreversal set ref_field_10 = (select status_text from tblstatusid where tblstatusid.lang_id=tbloutboundreversal.lang_id and tblstatusid.status_id=tbloutboundreversal.status_id )
GO
update tblordermangementline set ref_field_7 = (select status_text from tblstatusid where tblstatusid.lang_id=tblordermangementline.lang_id and tblstatusid.status_id=tblordermangementline.status_id )
GO
update tblpickupheader set ref_field_7 = (select status_text from tblstatusid where tblstatusid.lang_id=tblpickupheader.lang_id and tblstatusid.status_id=tblpickupheader.status_id )
GO
update tblcontainerreceipt set ref_field_9 = (select status_text from tblstatusid where tblstatusid.lang_id=tblcontainerreceipt.lang_id and tblstatusid.status_id=tblcontainerreceipt.status_id )
GO
update tblgrheader set ref_field_10 = (select status_text from tblstatusid where tblstatusid.lang_id=tblgrheader.lang_id and tblstatusid.status_id=tblgrheader.status_id )
GO
update tblpreoutboundheader set ref_field_10 = (select status_text from tblstatusid where tblstatusid.lang_id=tblpreoutboundheader.lang_id and tblstatusid.status_id=tblpreoutboundheader.status_id )
GO