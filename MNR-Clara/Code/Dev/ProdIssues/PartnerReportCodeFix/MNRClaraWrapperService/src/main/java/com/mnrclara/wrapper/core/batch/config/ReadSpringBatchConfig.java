package com.mnrclara.wrapper.core.batch.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.mnrclara.wrapper.core.repository.MatterGenAcc;

@EnableBatchProcessing
@Configuration
public class ReadSpringBatchConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public JpaPagingItemReader<MatterGenAcc> getJpaPagingItemReader()  {
        String sql = "select LANG_ID, CLASS_ID, MATTER_NO from tblmattergenaccid";
        JpaNativeQueryProvider<MatterGenAcc> queryProvider = new JpaNativeQueryProvider<MatterGenAcc>();
        JpaPagingItemReader<MatterGenAcc> reader = new JpaPagingItemReader<>();
        queryProvider.setSqlQuery(sql);
        reader.setQueryProvider(queryProvider);
        queryProvider.setEntityClass(MatterGenAcc.class);
//        reader.setParameterValues(Collections.singletonMap("limit", 1000));
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(2000);
        reader.setSaveState(true);
        return reader;
    }
    
//    @Bean
//    public JpaPagingItemReader getJpaPagingItemReader() {
//        return new JpaPagingItemReaderBuilder<MatterGenAcc>()
//                .name("MatterGenAcc")
//                .entityManagerFactory(entityManagerFactory)
//                .queryString("select LANG_ID, CLASS_ID, MATTER_NO from tblmattergenaccid s")
//                .pageSize(1000)
//                .build();
//    }

    @Bean
    public FlatFileItemWriter<MatterGenAcc> writer() {
        FlatFileItemWriter<MatterGenAcc> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("D:/Murugavel/Project/data.csv"));
        writer.setLineAggregator(getDelimitedLineAggregator());
        return writer;
    }

    private DelimitedLineAggregator<MatterGenAcc> getDelimitedLineAggregator() {
        BeanWrapperFieldExtractor<MatterGenAcc> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<MatterGenAcc>();
        beanWrapperFieldExtractor.setNames(new String[]{"languageId", "classId", "matterNumber"});
        DelimitedLineAggregator<MatterGenAcc> aggregator = new DelimitedLineAggregator<MatterGenAcc>();
        aggregator.setDelimiter(",");
        aggregator.setFieldExtractor(beanWrapperFieldExtractor);
        return aggregator;
    }

    @Bean
    public Step getDbToCsvStep() {
        StepBuilder stepBuilder = stepBuilderFactory.get("getDbToCsvStep");
        SimpleStepBuilder<MatterGenAcc, MatterGenAcc> simpleStepBuilder = stepBuilder.chunk(1000);
        return simpleStepBuilder.reader(getJpaPagingItemReader()).processor(processor()).writer(writer()).build();
    }

    @Bean
    public Job dbToCsvJob() {
        JobBuilder jobBuilder = jobBuilderFactory.get("dbToCsvJob");
        jobBuilder.incrementer(new RunIdIncrementer());
        FlowJobBuilder flowJobBuilder = jobBuilder.flow(getDbToCsvStep()).end();
        Job job = flowJobBuilder.build();
        return job;
    }

    @Bean
    public StudentItemProcessor processor() {
        return new StudentItemProcessor();
    }
}
