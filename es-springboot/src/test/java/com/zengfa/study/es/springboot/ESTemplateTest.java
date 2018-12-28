package com.zengfa.study.es.springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ESTemplateTest {

	@Autowired
    private ElasticsearchTemplate esTemplate;
	
	@Test
	public void testIndexExists() {
		boolean result = esTemplate.indexExists("resource");
		System.out.println(result);
	}
}
