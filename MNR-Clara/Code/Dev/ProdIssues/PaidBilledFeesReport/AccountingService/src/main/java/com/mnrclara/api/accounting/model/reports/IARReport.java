package com.mnrclara.api.accounting.model.reports;

import java.util.Date;

public interface IARReport {

    public Long getClassId();
    public String getClientId();
    public String getMatterNumber();
    public Date getPostingDate();
    
}
