package com.mnrclara.api.common.model.docketwise;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Contact {
	/*
	 * {
        "id": 4954959,
        "first_name": "Test",
        "last_name": "Test",
        "middle_name": "Test",
        "company_name": null,
        "email": "test@test.com",
        "created_at": "2021-12-16T06:00:57.442Z",
        "updated_at": "2021-12-16T06:00:57.442Z",
        "uuid": null,
        "ssn": null,
        "mailing_address": {
            "id": null,
            "street_number_and_name": null,
            "apartment_number": null,
            "apartment_suite_floor": null,
            "city": null,
            "state": null,
            "county": null,
            "postal_code": null,
            "province": null,
            "date_from": null,
            "date_to": null,
            "in_care_of": null,
            "zip_code": null,
            "created_at": null,
            "updated_at": null,
            "country": null
        },
        "physical_address": {
            "id": 5061779,
            "street_number_and_name": "This is and address",
            "apartment_number": null,
            "apartment_suite_floor": null,
            "city": null,
            "state": null,
            "county": null,
            "postal_code": null,
            "province": null,
            "date_from": null,
            "date_to": null,
            "in_care_of": null,
            "zip_code": null,
            "created_at": "2021-12-16T06:00:57.448Z",
            "updated_at": "2021-12-16T06:00:57.448Z",
            "country": null
        },
        "phone_numbers": [
            {
                "id": 2808254,
                "number": "1234567",
                "country_code": null,
                "number_type": null,
                "other_numberable_id": 4954959,
                "other_numberable_type": "Contact",
                "created_at": "2021-12-16T06:00:57.450Z",
                "updated_at": "2021-12-16T06:00:57.450Z",
                "data": {
                    "number": "1234567",
                    "daytime": "true"
                },
                "created_by_migration": null
            }
        ]
    }
	 */
	private Long id;
	private String first_name;
	private String last_name;
	private String middle_name;
	private String company_name;
	private String email;
	private String type; // 'Person' or 'Institution'
	private Date created_at;
	private Date updated_at;
	private boolean lead;
	private List<AddressAttribute> addresses_attributes;
	private List<PhoneNumberAttribute> phone_numbers_attributes;
}
