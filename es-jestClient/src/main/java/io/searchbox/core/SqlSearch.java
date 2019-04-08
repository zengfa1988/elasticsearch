package io.searchbox.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.searchbox.action.AbstractAction;
import io.searchbox.action.AbstractMultiTypeActionBuilder;
import io.searchbox.client.config.ElasticsearchVersion;

/**
 * ES sql查询
 * @author Thinkpad
 *
 */
public class SqlSearch extends AbstractAction<SqlSearchResult>{

	private String sql;
	
	protected SqlSearch(Builder builder) {
		super(builder);
		this.sql = builder.sql;
	}
	
	@Override
	public SqlSearchResult createNewElasticSearchResult(String responseBody, int statusCode, String reasonPhrase,
			Gson gson) {
		return createNewElasticSearchResult(new SqlSearchResult(gson), responseBody, statusCode, reasonPhrase, gson);
	}

	@Override
	public String getRestMethodName() {
		return "POST";
	}

	@Override
	protected String buildURI(ElasticsearchVersion elasticsearchVersion) {
		return "_xpack/sql?format=json";
	}
	
	@Override
	public String getData(Gson gson) {
		JsonObject queryObject = new JsonObject();
		queryObject.addProperty("query", sql);
		return gson.toJson(queryObject);
	}

	public static class Builder extends AbstractMultiTypeActionBuilder<SqlSearch, Builder>{
		protected String sql;
		public Builder(String sql) {
			this.sql = sql;
		}
		
		@Override
		public SqlSearch build() {
			return new SqlSearch(this);
		}
		
	}

}
