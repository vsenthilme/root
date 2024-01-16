package com.mnrclara.api.cg.transaction.model.storepartnerlisting;


import lombok.Data;

@Data
public class MatchResultResponse {

    private Long coOwnerId1;

    private Long coOwnerId2;

    private Long coOwnerId3;

    private Long coOwnerId4;

    private Long coOwnerId5;

    private Long coOwnerId6;

    private Long coOwnerId7;

    private Long coOwnerId8;

    private Long coOwnerId9;

    private Long coOwnerId10;

    private String coOwnerName1;

    private String coOwnerName2;

    private String coOwnerName3;

    private String coOwnerName4;

    private String coOwnerName5;

    private String coOwnerName6;

    private String coOwnerName7;

    private String coOwnerName8;

    private String coOwnerName9;

    private String coOwnerName10;

    private Double coOwnerPercentage1;

    private Double coOwnerPercentage2;

    private Double coOwnerPercentage3;

    private Double coOwnerPercentage4;

    private Double coOwnerPercentage5;

    private Double coOwnerPercentage6;

    private Double coOwnerPercentage7;

    private Double coOwnerPercentage8;

    private Double coOwnerPercentage9;

    private Double coOwnerPercentage10;

    private String storeId;

    private String storeName;


    public Long getCoOwnerId(int i) {
        switch (i) {
            case 1:
                return getCoOwnerId1();
            case 2:
                return getCoOwnerId2();
            case 3:
                return getCoOwnerId3();
            case 4:
                return getCoOwnerId4();
            case 5:
                return getCoOwnerId5();
            case 6:
                return getCoOwnerId6();
            case 7:
                return getCoOwnerId7();
            case 8:
                return getCoOwnerId8();
            case 9:
                return getCoOwnerId9();
            case 10:
                return getCoOwnerId10();
            default:
                return null;
        }
    }

    public String getCoOwnerName(int i) {
        switch (i) {
            case 1:
                return getCoOwnerName1();
            case 2:
                return getCoOwnerName2();
            case 3:
                return getCoOwnerName3();
            case 4:
                return getCoOwnerName4();
            case 5:
                return getCoOwnerName5();
            case 6:
                return getCoOwnerName6();
            case 7:
                return getCoOwnerName7();
            case 8:
                return getCoOwnerName8();
            case 9:
                return getCoOwnerName9();
            case 10:
                return getCoOwnerName10();
            default:
                return null;
        }
    }

    public Double getCoOwnerPercentage(int i) {
        switch (i) {
            case 1:
                return getCoOwnerPercentage1();
            case 2:
                return getCoOwnerPercentage2();
            case 3:
                return getCoOwnerPercentage3();
            case 4:
                return getCoOwnerPercentage4();
            case 5:
                return getCoOwnerPercentage5();
            case 6:
                return getCoOwnerPercentage6();
            case 7:
                return getCoOwnerPercentage7();
            case 8:
                return getCoOwnerPercentage8();
            case 9:
                return getCoOwnerPercentage9();
            case 10:
                return getCoOwnerPercentage10();
            default:
                return null;
        }
    }


}
