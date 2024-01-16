USE WMS
GO
update tblgrheader set REF_FIELD_10 = st.status_text from tblgrheader gr join tblstatusid st on gr.status_id = st.status_id and gr.lang_id=st.lang_id
GO
update tblpreoutboundheader set REF_FIELD_10 = st.status_text from tblpreoutboundheader gr join tblstatusid st on gr.status_id = st.status_id and gr.lang_id=st.lang_id
GO
update tblpreinboundheader set REF_FIELD_10 = st.status_text from tblpreinboundheader gr join tblstatusid st on gr.status_id = st.status_id and gr.lang_id=st.lang_id
GO
update tblstagingheader set REF_FIELD_10 = st.status_text from tblstagingheader gr join tblstatusid st on gr.status_id = st.status_id and gr.lang_id=st.lang_id
GO
update tblputawayheader set REF_FIELD_10 = st.status_text from tblputawayheader gr join tblstatusid st on gr.status_id = st.status_id and gr.lang_id=st.lang_id
GO
update tblinboundheader set REF_FIELD_10 = st.status_text from tblinboundheader gr join tblstatusid st on gr.status_id = st.status_id and gr.lang_id=st.lang_id
GO
update tblqualityheader set REF_FIELD_10 = st.status_text from tblqualityheader gr join tblstatusid st on gr.status_id = st.status_id and gr.lang_id=st.lang_id
GO
update tbloutboundreversal set REF_FIELD_10 = st.status_text from tbloutboundreversal gr join tblstatusid st on gr.status_id = st.status_id and gr.lang_id=st.lang_id
GO
update tblordermangementline set REF_FIELD_7 = st.status_text from tblordermangementline gr join tblstatusid st on gr.status_id = st.status_id and gr.lang_id=st.lang_id
GO
update tblpickupheader set REF_FIELD_7 = st.status_text from tblpickupheader gr join tblstatusid st on gr.status_id = st.status_id and gr.lang_id=st.lang_id
GO
update tblcontainerreceipt set REF_FIELD_9 = st.status_text from tblcontainerreceipt gr join tblstatusid st on gr.status_id = st.status_id and gr.lang_id=st.lang_id
GO