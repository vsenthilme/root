package com.mnrclara.wrapper.core.batch.config;

import org.springframework.batch.item.ItemProcessor;

import com.mnrclara.wrapper.core.repository.MatterGenAcc;


public class StudentItemProcessor implements ItemProcessor<MatterGenAcc, MatterGenAcc> {

    @Override
    public MatterGenAcc process(MatterGenAcc student) throws Exception {
        return student;
    }
}