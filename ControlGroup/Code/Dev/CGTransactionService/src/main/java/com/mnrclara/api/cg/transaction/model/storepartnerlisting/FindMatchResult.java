package com.mnrclara.api.cg.transaction.model.storepartnerlisting;


import lombok.Data;

@Data
public class FindMatchResult {

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
    private String groupId;

    public Long getCoOwnerId(int i) {

        switch (i) {
            case 1:
                return coOwnerId1;
            case 2:
                return coOwnerId2;
            case 3:
                return coOwnerId3;
            case 4:
                return coOwnerId4;
            case 5:
                return coOwnerId5;
            case 6:
                return coOwnerId6;
            case 7:
                return coOwnerId7;
            case 8:
                return coOwnerId8;
            case 9:
                return coOwnerId9;
            case 10:
                return coOwnerId10;
            default:
                break;
        }
        return null;
    }
}
