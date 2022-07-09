package com.example.elastic.service;

import com.example.elastic.domain.Any;
import com.example.elastic.dto.SearchRequestDTO;
import com.example.elastic.util.Indices;
import com.example.elastic.util.SearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AnyService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(AnyService.class);

    private final RestHighLevelClient client;

    @Autowired
    public AnyService(RestHighLevelClient client) {
        this.client = client;
    }


    public List<Any> search(final SearchRequestDTO dto) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.ANY_INDEX,
                dto
        );

        return searchInternal(request);
    }


    public List<Any> getAllAnyCreatedSince(final Date date) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.ANY_INDEX,
                "created",
                date
        );

        return searchInternal(request);
    }

    public List<Any> searchCreatedSince(final SearchRequestDTO dto, final Date date) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.ANY_INDEX,
                dto,
                date
        );

        return searchInternal(request);
    }

    private List<Any> searchInternal(final SearchRequest request) {
        if (request == null) {
            LOG.error("Failed to build search request");
            return Collections.emptyList();
        }

        try {
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            final SearchHit[] searchHits = response.getHits().getHits();
            final List<Any> any = new ArrayList<>(searchHits.length);
            for (SearchHit hit : searchHits) {
                any.add(
                        MAPPER.readValue(hit.getSourceAsString(), Any.class)
                );
            }

            return any;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Boolean index(final Any any) {
        try {
            final String anyAsString = MAPPER.writeValueAsString(any);

            final IndexRequest request = new IndexRequest(Indices.ANY_INDEX);
            request.id(any.getId());
            request.source(anyAsString, XContentType.JSON);

            final IndexResponse response = client.index(request, RequestOptions.DEFAULT);

            return response != null && response.status().equals(RestStatus.OK);
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    public Any getById(final String anyId) {
        try {
            final GetResponse documentFields = client.get(
                    new GetRequest(Indices.ANY_INDEX, anyId),
                    RequestOptions.DEFAULT
            );
            if (documentFields == null || documentFields.isSourceEmpty()) {
                return null;
            }

            return MAPPER.readValue(documentFields.getSourceAsString(), Any.class);
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }
}