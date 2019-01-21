package com.zengfa.study.es;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.config.HttpClientConfig.Builder;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Cat;
import io.searchbox.core.Cat.IndicesBuilder;
import io.searchbox.core.Count;
import io.searchbox.core.CountResult;
import io.searchbox.core.Delete;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.Update;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.Stats;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.strings.StringUtils;

/**
 * https://www.cnblogs.com/enenen/p/9122053.html
 * https://blog.csdn.net/k0307x1990y/article/details/81369684
 * https://blog.csdn.net/u011781521/article/details/77852861
 * @author Thinkpad
 *
 */
public class JestTest {

	private static JestClient jestClient;
	private static String elasticIps = "http://192.168.0.112:9200/";
	private static String userName;
	private static String password;
	
	public static void main(String[] args) {
		initClient();
		try {
//			createIndex();
//			deleteIndex();
//			createIndexMapping();
//			indicesExists();
//			addDoc();
//			addBulkDoc();
//			getDoc();
//			updateDoc();
//			deleteDoc();
//			searchDoc();
//			rangeQuery();
//			count();
			listIndices();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void initClient() {
		JestClientFactory factory = new JestClientFactory();
		Set<String> serverUris = new LinkedHashSet<String>(Arrays.asList(elasticIps.split(",")));
		Builder builder = new HttpClientConfig.Builder(serverUris)
				.connTimeout(60000).readTimeout(60000).multiThreaded(true);
		if(StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
			builder.defaultCredentials(userName, password);
		}
		factory.setHttpClientConfig(builder.build());
		jestClient = factory.getObject();
	}
	
	/**
	 * 创建索引
	 * @throws Exception
	 */
	private static void createIndex() throws Exception{
		String index = "resource";
        
		CreateIndex createIndex = new CreateIndex.Builder(index).build();
		JestResult jestResult = jestClient.execute(createIndex);
		System.out.println(jestResult.isSucceeded());
	}
	
	/**
	 * 删除索引
	 * @throws Exception
	 */
	private static void deleteIndex() throws Exception{
		String index = "resource";
		DeleteIndex deleteIndex = new DeleteIndex.Builder(index).build();
		JestResult jestResult = jestClient.execute(deleteIndex);
		System.out.println(jestResult.isSucceeded());
	}
	
	/**
	 * 先要创建索引，再设置mapping
	 * @throws Exception
	 */
	private static void createIndexMapping() throws Exception{
		String index = "resource";
		String type = "doc";
		
		JSONObject objSource = new JSONObject().fluentPut("properties", new JSONObject()
				.fluentPut("title", new JSONObject().fluentPut("type", "text"))
				.fluentPut("author", new JSONObject().fluentPut("type", "text"))
				.fluentPut("content", new JSONObject().fluentPut("type", "text"))
				.fluentPut("publishDate", new JSONObject().fluentPut("type", "date")));
		
		String mappingString = objSource.toJSONString();
		
		PutMapping putMapping = new PutMapping.Builder(index, type, mappingString).build();
		JestResult jestResult = jestClient.execute(putMapping);
		System.out.println(jestResult.isSucceeded());
	}
	
	public static void indicesExists() throws Exception{
		String index = "resource2";
		IndicesExists indicesExists = new IndicesExists.Builder(index).build();
		JestResult jestResult = jestClient.execute(indicesExists);
		System.out.println(jestResult.isSucceeded());
	}
	
	/**
	 * 列出所有索引
	 * @throws Exception
	 */
	public static void listIndices() throws Exception{
		Cat cat = new IndicesBuilder().build();
		JestResult jestResult = jestClient.execute(cat);
		if(jestResult.isSucceeded()) {
			String indiceStr = jestResult.getSourceAsString();
			List<Map> mapList = JSONArray.parseArray(indiceStr, Map.class);
			for(Map indexMap : mapList) {
//				System.out.println(JSONObject.toJSONString(indexMap));
				String indexName = (String)indexMap.get("index");
				System.out.println(indexName);
			}
//			System.out.println(JSONArray.toJSONString(mapList));
		}
	}
	
	/**
	 * 添加一个文档
	 * @throws Exception
	 */
	public static void addDoc() throws Exception{
		String indexName = "resource";
		String typeName = "doc";
		Resource resource = new Resource();
		resource.setTitle("中国获租巴基斯坦瓜达尔港2000亩土地 为期43年");
		resource.setAuthor("CCCC");
		resource.setContent("据了解，瓜达尔港务局于今年6月完成了1500亩土地的征收工作，另外500亩的征收工作也将很快完成");
		resource.setPublishDate(new Date());
		Index index = new Index.Builder(resource).index(indexName).type(typeName).build();
		JestResult jestResult = jestClient.execute(index);
		System.out.println(jestResult.isSucceeded());
	}
	
	/**
	 * 批量添加
	 * @throws Exception
	 */
	public static void addBulkDoc() throws Exception{
		String indexName = "resource";
		String typeName = "doc";
		List<Index> indexList = new ArrayList<Index>();
		for(int i=0;i<10;i++) {
			Resource resource = new Resource();
			resource.setTitle("中国获租巴基斯坦瓜达尔港2000亩土地 为期43年");
			resource.setAuthor("CCCC");
			resource.setContent("据了解，瓜达尔港务局于今年6月完成了1500亩土地的征收工作，另外500亩的征收工作也将很快完成");
			resource.setPublishDate(new Date());
			Index index = new Index.Builder(resource).index(indexName).type(typeName).build();
			indexList.add(index);
		}
		Bulk bulk = new Bulk.Builder().addAction(indexList).build();
        BulkResult jestResult = jestClient.execute(bulk);
        System.out.println(jestResult.isSucceeded());
	}
	
	public static void getDoc() throws Exception{
		String indexName = "resource";
		Get get = new Get.Builder(indexName, "mUds-mcBZk-2kIgh-pNL").build();
		JestResult result = jestClient.execute(get);
		if(result.isSucceeded()) {
			Resource resource = result.getSourceAsObject(Resource.class);
			System.out.println(JSONObject.toJSONString(resource));
		}
	}
	
	public static void updateDoc() throws Exception{
		Resource resource = new Resource();
		resource.setTitle("中国获租巴基斯坦瓜达尔港2000亩土地");
		resource.setContent("csdcd");
		resource.setAuthor("zengfa");
		resource.setPublishDate(new Date());
		String indexName = "resource";
		String typeName = "doc";
		Update update = new Update.Builder(resource).index(indexName)
				.type(typeName).id("mUds-mcBZk-2kIgh-pNL").build();
		JestResult result = jestClient.execute(update);
		System.out.println(result.isSucceeded());
	}
	
	public static void deleteDoc() throws Exception{
		String indexName = "resource";
		String typeName = "doc";
		Delete delete = new Delete.Builder("mUds-mcBZk-2kIgh-pNL").index(indexName)
			.type(typeName).build();
		JestResult result = jestClient.execute(delete);
		System.out.println(result.isSucceeded());
	}
	
	/**
	 * 单值完全匹配查询
	 * @throws Exception
	 */
	public static void searchDoc() throws Exception{
		String indexName = "resource";
		String typeName = "doc";
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders  
                .termQuery("author", "CCCC".toLowerCase());//单值完全匹配查询  
        searchSourceBuilder.query(queryBuilder);  
        searchSourceBuilder.size(10);  
        searchSourceBuilder.from(0);  
        String query = searchSourceBuilder.toString();   
        System.out.println(query); 
        Search search = new Search.Builder(query)  
                .addIndex(indexName)  
                .addType(typeName)  
                .build();  
        SearchResult result = jestClient.execute(search); 
        
        List<Hit<Resource, Void>> hits = result.getHits(Resource.class);  
        System.out.println("Size:" + hits.size());  
        for (Hit<Resource, Void> hit : hits) {  
        	Resource resource = hit.source;  
          System.out.println(JSONObject.toJSONString(resource));  
        }  
	}
	
	private static void rangeQuery() throws Exception { 
		String indexName = "resource";
		String typeName = "doc";
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder queryBuilder = QueryBuilders  
	            .rangeQuery("publishDate")  
	            .gte("2018-12-01T00:00:00")  
	            .lte("2019-01-01T00:00:00")  
	            .includeLower(true)  
	            .includeUpper(true);//区间查询  
		searchSourceBuilder.query(queryBuilder);  
        searchSourceBuilder.size(10);  
        searchSourceBuilder.from(0);  
        String query = searchSourceBuilder.toString();
		Search search = new Search.Builder(query)  
                .addIndex(indexName)  
                .addType(typeName)  
                .build();  
        SearchResult result = jestClient.execute(search); 
        
        List<Hit<Object, Void>> hits = result.getHits(Object.class);  
        System.out.println("Size:" + hits.size());  
        for (Hit<Object, Void> hit : hits) {  
          Object news = hit.source;  
          System.out.println(news.toString());  
        }  
	}
	
	private static void count() throws Exception { 
		String indexName = "resource";
		String typeName = "doc";
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		QueryBuilder queryBuilder = QueryBuilders  
                .termQuery("author", "CCCC".toLowerCase());//单值完全匹配查询  
        searchSourceBuilder.query(queryBuilder);  
        String query = searchSourceBuilder.toString();   
		Count count = new Count.Builder()  
                .addIndex(indexName)  
                .addType(typeName)  
                .query(query)  
                .build();  
        CountResult results = jestClient.execute(count);   
        
        Double counts = results.getCount();
        System.out.println("Count:" + counts);  
	}
	
}
