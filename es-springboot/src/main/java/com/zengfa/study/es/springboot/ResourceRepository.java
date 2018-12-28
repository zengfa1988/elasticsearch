package com.zengfa.study.es.springboot;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ResourceRepository extends ElasticsearchRepository<Resource, Integer>{

	List<Resource> findByReadNumberBetween(Integer readNumber1,Integer readNumber2);
	
	List<Resource> findByType(String type);
}
