package com.iwmvp.api.master.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseService {

    /**
     *
     * @return
     */
    protected String getLanguageId () {
        return "EN";
    }

    /**
     *
     * @return
     */
    protected String getCompanyId () {
        return "1000";
    }

}
