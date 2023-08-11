package com.example.springbatchdemo.batch;

import com.example.springbatchdemo.model.Alert;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import javax.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Value("${app.batch.file}")
    private String batchFileName;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public FlatFileItemReader<Alert> csvReader(){

        FlatFileItemReader<Alert> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(batchFileName)); // Specify your CSV file path
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("id",
                        "name",
                        "type",
                        "email",
                        "sms");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(Alert.class);
            }});
        }});
        return reader;
    }

    @Bean
    public JpaItemWriter<Alert> mysqlWriter() {
        JpaItemWriter<Alert> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Job importJob(JobBuilderFactory jobs, Step step) {
        return jobs.get("importJob")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public ItemProcessor<Alert, Alert> consoleItemProcessor() {
        return person -> {
            System.out.println("Processing: " + person.getName() + " " + person.getEmail());
            return person;
        };
    }


    @Bean
    public Step step(StepBuilderFactory stepBuilderFactory,
                     ItemReader<Alert> reader, ItemWriter<Alert> writer) {
        return stepBuilderFactory.get("step")
                .<Alert, Alert>chunk(10)
                .reader(reader)
                .processor(consoleItemProcessor())
                .writer(writer)
                .build();
    }

}
