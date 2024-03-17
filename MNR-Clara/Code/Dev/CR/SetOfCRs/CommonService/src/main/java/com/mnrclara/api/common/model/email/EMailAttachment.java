package com.mnrclara.api.common.model.email;

import lombok.Data;

import java.io.Serializable;


@Data
public class EMailAttachment implements Serializable {

    private String fileName;
    private byte[] content;

    public EMailAttachment(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }

}
