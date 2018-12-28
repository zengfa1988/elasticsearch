package com.zengfa.study.es.springboot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResourceRepositoryTest {

	@Autowired
	private ResourceRepository resourceRepository;
	
	@Test
	public void testSave() {
		Resource resource = new Resource();
		resource.setId(1);
		resource.setName("SpringBoot整合elasticsearch");
		resource.setType("blog");
		resource.setContent("在这一篇文章开始之前，你需要先安装一个ElasticSearch，如果你是mac或者linux可以参考https://www.jianshu.com/p/e47b451375ea，如果是windows可以自定百度一下。");
		resource.setCreateTime(new Date());
		resource.setPublishTime(new Date());
		resource.setReadNumber(1000);
		resource.setLikeNumber(20);
		resource.setResource("https://www.cnblogs.com/dalaoyang/p/8990989.html");
		resource.setUrl("https://www.jianshu.com/p/491d34d8349a");
		resource.setWriter("ilaoke");
		resourceRepository.save(resource);
	}
	
	/**
	 * 修改和新增是同一个接口，区分的依据就是id。
	 */
	@Test
	public void testUpdate() {
		Resource resource = new Resource();
		resource.setId(2);
//		resource.setName("SpringBoot整合elasticsearch");
//		resource.setType("blog");
		resource.setContent("Spring Data 的强大之处，就在于你不用写任何DAO处理，自动根据方法名或类的信息进行CRUD操作。只要你定义一个接口，然后继承Repository提供的一些子接口，就能具备各种基本的CRUD功能。");
		resource.setUpdateTime(new Date());
//		resource.setCreateTime(new Date());
//		resource.setPublishTime(new Date());
//		resource.setReadNumber(1000);
//		resource.setLikeNumber(20);
//		resource.setResource("https://www.cnblogs.com/dalaoyang/p/8990989.html");
//		resource.setUrl("https://www.jianshu.com/p/491d34d8349a");
//		resource.setWriter("ilaoke");
		resourceRepository.save(resource);
	}
	
	@Test
	public void testSaveAll() {
		List<Resource> list = new ArrayList<Resource>();
		Resource resource1 = new Resource();
		resource1.setId(2);
		resource1.setName("elasticsearch");
		resource1.setType("blog");
		resource1.setCreateTime(new Date());
		resource1.setPublishTime(new Date());
		resource1.setReadNumber(1200);
		resource1.setLikeNumber(50);
		resource1.setWriter("ilaoke");
		list.add(resource1);
		
		Resource resource2 = new Resource();
		resource2.setId(3);
		resource2.setName("springboot");
		resource2.setType("blog");
		resource2.setCreateTime(new Date());
		resource2.setPublishTime(new Date());
		resource2.setReadNumber(800);
		resource2.setLikeNumber(10);
		resource2.setWriter("ilaoke");
		list.add(resource2);
		
		resourceRepository.saveAll(list);
	}
	
	@Test
	public void testFindById() {
		Resource resource = resourceRepository.findById(1).get();
		System.out.println(JSONObject.toJSONString(resource));
	}
	
	@Test
	public void testSearch() {
		Resource resource = new Resource();
		resource.setType("blog");
		QueryBuilder query = new TermQueryBuilder("type", resource.getType());
//		Iterable<Resource> it = resourceRepository.search(query);
		Iterable<Resource> it = resourceRepository.search(new MatchQueryBuilder("content", "百度"));
		for(Resource r : it) {
			System.out.println(JSONObject.toJSONString(r));
		}
	}
	
	@Test
	public void testFindByTypen() {
		List<Resource> list = resourceRepository.findByType("blog2");
		for(Resource r : list) {
			System.out.println(JSONObject.toJSONString(r));
		}
	}
	
	@Test
	public void testFindByReadNumberBetween() {
		List<Resource> list = resourceRepository.findByReadNumberBetween(900, 1200);
		for(Resource r : list) {
			System.out.println(JSONObject.toJSONString(r));
		}
	}
}
