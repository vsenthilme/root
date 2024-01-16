package com.mnrclara.wrapper.core.batch.mapper;

import com.mnrclara.wrapper.core.batch.dto.StorePartnerListing;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class StorePartnerListingFieldSetMapper implements FieldSetMapper<StorePartnerListing> {

	@Override
	public StorePartnerListing mapFieldSet(FieldSet fieldSet) {
		return new StorePartnerListing(
			fieldSet.readString("languageId"),
			fieldSet.readString(	"companyId"),
			fieldSet.readString("storeId"),
			fieldSet.readLong  (	"versionNumber"),
			fieldSet.readString(	"storeName"),
			fieldSet.readDate  ("validityDateFrom"),
			fieldSet.readDate  ("validityDateTo"),
			fieldSet.readString(	"groupTypeId"),
			fieldSet.readString("groupTypeName"),
			fieldSet.readString("subGroupId"),
			fieldSet.readString("subGroupName"),
			fieldSet.readString("groupId"),
			fieldSet.readString("groupName"),
			fieldSet.readLong  ("coOwnerId1"),
			fieldSet.readLong  ("coOwnerId2"),
			fieldSet.readLong  ("coOwnerId3"),
			fieldSet.readLong  ("coOwnerId4"),
			fieldSet.readLong  ("coOwnerId5"),
			fieldSet.readString("coOwnerName1"),
			fieldSet.readString("coOwnerName2"),
			fieldSet.readString("coOwnerName3"),
			fieldSet.readString("coOwnerName4"),
			fieldSet.readString("coOwnerName5"),
			fieldSet.readDouble("coOwnerPercentage1"),
			fieldSet.readDouble("coOwnerPercentage2"),
			fieldSet.readDouble("coOwnerPercentage3"),
			fieldSet.readDouble("coOwnerPercentage4"),
			fieldSet.readDouble("coOwnerPercentage5"),
			fieldSet.readLong  ("statusId"),
			fieldSet.readLong  ("deletionIndicator"),
			fieldSet.readString("referenceField1"),
			fieldSet.readString("referenceField2"),
			fieldSet.readString("referenceField3"),
			fieldSet.readString("referenceField4"),
			fieldSet.readString("referenceField5"),
			fieldSet.readString("referenceField6"),
			fieldSet.readString("referenceField7"),
			fieldSet.readString("referenceField8"),
			fieldSet.readString("referenceField9"),
			fieldSet.readString("referenceField10"),
			fieldSet.readString("createdBy"),
			fieldSet.readDate  ("createdOn"),
			fieldSet.readString("updatedBy"),
			fieldSet.readDate  ("updatedOn")
		);
	}
}