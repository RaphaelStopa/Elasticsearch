package com.example.elastic.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.core.convert.converter.Converter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import java.time.ZonedDateTime;
import java.util.Arrays;


@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.example.elastic.repository")
//@ComponentScan(basePackages = {"com.example.elastic"}) // este e o de cima  ver se eh nescessario
// troquei AbstractElasticsearchConfiguration por ElasticsearchConfigurationSupport
public class ConfigElastic extends ElasticsearchConfigurationSupport {

//    @Value("${elasticsearch.url}")
//    public String elasticsearchUrl;
//
//    @Bean
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        final ClientConfiguration config = ClientConfiguration.builder()
//                .connectedTo(elasticsearchUrl)
//                .build();
//
//        return RestClients.create(config).rest();
//    }
//


    @Bean
    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(
                Arrays.asList(
                        new ZonedDateTimeWritingConverter(),
                        new ZonedDateTimeReadingConverter(),
                        new InstantWritingConverter(),
                        new InstantReadingConverter(),
                        new LocalDateWritingConverter(),
                        new LocalDateReadingConverter()
                )
        );
    }

    @WritingConverter
    static class ZonedDateTimeWritingConverter implements Converter<ZonedDateTime, String> {

        @Override
        public String convert(ZonedDateTime source) {
            if (source == null) {
                return null;
            }
            return source.toInstant().toString();
        }
    }

    @ReadingConverter
    static class ZonedDateTimeReadingConverter implements Converter<String, ZonedDateTime> {

        @Override
        public ZonedDateTime convert(String source) {
            if (source == null) {
                return null;
            }
            return Instant.parse(source).atZone(ZoneId.systemDefault());
        }
    }

    @WritingConverter
    static class InstantWritingConverter implements Converter<Instant, String> {

        @Override
        public String convert(Instant source) {
            if (source == null) {
                return null;
            }
            return source.toString();
        }
    }

    @ReadingConverter
    static class InstantReadingConverter implements Converter<String, Instant> {

        @Override
        public Instant convert(String source) {
            if (source == null) {
                return null;
            }
            return Instant.parse(source);
        }
    }

    @WritingConverter
    static class LocalDateWritingConverter implements Converter<LocalDate, String> {

        @Override
        public String convert(LocalDate source) {
            if (source == null) {
                return null;
            }
            return source.toString();
        }
    }

    @ReadingConverter
    static class LocalDateReadingConverter implements Converter<String, LocalDate> {

        @Override
        public LocalDate convert(String source) {
            if (source == null) {
                return null;
            }
            return LocalDate.parse(source);
        }
    }
}
