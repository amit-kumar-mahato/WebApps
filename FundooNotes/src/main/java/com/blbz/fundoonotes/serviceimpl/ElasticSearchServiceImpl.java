package com.blbz.fundoonotes.serviceimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blbz.fundoonotes.configuration.ElasticSearchConfig;
import com.blbz.fundoonotes.model.Note;
import com.blbz.fundoonotes.service.ElasticSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ElasticSearchConfig config;
	
	private String INDEX = "springboot";
	private String TYPE = "note_details";

	@Override
	public String CreateNote(Note note) {
		Map dataMap = objectMapper.convertValue(note, Map.class);
		IndexRequest indexrequest = new IndexRequest(INDEX, TYPE, String.valueOf(note.getNoteId())).source(dataMap);
		IndexResponse indexResponse = null;
		try {
			indexResponse = config.client().index(indexrequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(indexrequest);
		System.out.println(indexResponse);
		System.out.println( indexResponse.getResult().name());
		return indexResponse.getResult().name();
	}

	@Override
	public String UpdateNote(Note note) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String DeleteNote(Note note) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Note> searchbytitle(String title) {
		SearchRequest searchRequest = new SearchRequest("springboot");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.matchQuery("title", title));
		searchRequest.source(sourceBuilder);
		SearchResponse searchresponse = null;
		try {
			searchresponse = config.client().search(searchRequest, RequestOptions.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(getResult(searchresponse).toString());
		return getResult(searchresponse);
	}
	
	private List<Note> getResult(SearchResponse searchresponse) {
		SearchHit[] searchhits = searchresponse.getHits().getHits();
		List<Note> notes = new ArrayList<>();
		if (searchhits.length > 0) {
			Arrays.stream(searchhits)
					.forEach(hit -> notes.add(objectMapper.convertValue(hit.getSourceAsMap(), Note.class)));
		}
		return notes;
	}

	
}
