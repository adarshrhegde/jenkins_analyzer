package com.uic.atse.service;

import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.model.QueryType;
import com.uic.atse.model.Repository;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;

import java.util.List;
import java.util.Map;

/**
 * Empty interface to define the Type of remote service used Eg - Github
 */
public class RepositoryQuery {

    public String baseUrl;

    public String path;

    public String queryString;

    public String searchPath;

    public String searchType;

    public String ref;

    public List<Header> requestHeaders;

    public QueryType queryType;

    public int page;

    public RepositoryQuery(){}

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getSearchPath() {
        return searchPath;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getRef() {
        return ref;
    }

    public List<Header> getRequestHeaders() {
        return requestHeaders;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public int getPage() {
        return page;
    }

    public HttpGet createRequest() throws PipelineAnalyzerException {
        return null;
    }

    public List<Repository> convertResponse(List<StringBuilder> content) throws PipelineAnalyzerException {
        return null;
    }
}
