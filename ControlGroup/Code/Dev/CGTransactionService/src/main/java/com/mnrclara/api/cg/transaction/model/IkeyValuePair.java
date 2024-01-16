package com.mnrclara.api.cg.transaction.model;

public interface IkeyValuePair {


    Long getEntityId();
    Long getCoOwnerId1();

    Long getCoOwnerId2();

    Long getCoOwnerId3();

    Long getCoOwnerId4();

    Long getCoOwnerId5();

    Long getCoOwnerId6();

    Long getCoOwnerId7();

    Long getCoOwnerId8();

    Long getCoOwnerId9();

    Long getCoOwnerId10();

    String getCoOwnerName1();

    String getCoOwnerName2();

    String getCoOwnerName3();

    String getCoOwnerName4();

    String getCoOwnerName5();

    String getCoOwnerName6();

    String getCoOwnerName7();

    String getCoOwnerName8();

    String getCoOwnerName9();

    String getCoOwnerName10();

    Double getCoOwnerPercentage1();

    Double getCoOwnerPercentage2();

    Double getCoOwnerPercentage3();

    Double getCoOwnerPercentage4();

    Double getCoOwnerPercentage5();

    Double getCoOwnerPercentage6();

    Double getCoOwnerPercentage7();

    Double getCoOwnerPercentage8();

    Double getCoOwnerPercentage9();

    Double getCoOwnerPercentage10();

    String getStoreId();

    String getStoreName();

    String getGroupId();

    String getGroupName();

    String getGroupTypeId();

    String getSubGroupTypeId();

    String getGroupTypeName();

    String getSubGroupTypeName();


    public default Long getCoOwnerId(int i) {
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

    public default String getCoOwnerName(int i) {
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

    public default Double getCoOwnerPercentage(int i) {
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
