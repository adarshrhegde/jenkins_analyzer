package com.uic.atse.impl;

import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.model.QueryType;
import com.uic.atse.model.Repository;
import com.uic.atse.service.RepositoryQuery;
import com.uic.atse.utils.PipelineAnalyzerProperties;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to query github using the details conforming to Github Developer API
 */
public class GithubRepositoryQuery extends RepositoryQuery {


    Logger logger = Logger.getLogger(GithubRepositoryQuery.class.getName());

    public GithubRepositoryQuery(QueryType queryType) throws PipelineAnalyzerException {
        super();
        if (queryType == null){
            throw new PipelineAnalyzerException("Query type not specified for while querying Github");
        }

        if(queryType == QueryType.jenkinsFile){
            this.queryType = queryType;
            PipelineAnalyzerProperties props = PipelineAnalyzerProperties.getInstance();
            this.path = props.getGithubPath();
            this.baseUrl = props.getGithubBaseUrl();
            this.queryString = props.getGithubQueryString();
            this.ref = props.getGithubRef();
            this.searchPath = props.getGithubSearchPath();
            this.searchType = props.getGithubSearchType();
            this.requestHeaders = new ArrayList<>();
            this.requestHeaders.add(new BasicHeader("Authorization",props.getGithubAuthorization()));
            this.requestHeaders.add(new BasicHeader("accept", "application/json"));
            this.requestHeaders.add(new BasicHeader("user-agent", props.getGithubUserAgent()));

        }
    }

    public HttpGet createRequest() {

        // https://api.github.com/search/code?q=jenkinsfile.txt+in:path&type=Code&ref=searchresults

        String url = getBaseUrl() + getPath() +
                "?q=" + getQueryString() + "+" + getSearchPath() +
                "&type=" + getSearchType() + "&ref=" + getRef();
        logger.info("Query Url = "+ url);

        return createRequestFromUrl(url);

    }

    private HttpGet createRequestFromUrl(String url) {

        HttpGet request = new HttpGet(url);
        for(Header header : getRequestHeaders())
            request.addHeader(header);

        return request;

    }

    public List<Repository> convertResponse(StringBuilder content) throws PipelineAnalyzerException {
        List<Repository> repositoryList = new ArrayList<>();

        try {

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(content.toString());

            if(null != jsonResponse.get("items")) {

                JSONArray responseItems = (JSONArray) jsonResponse.get("items");

                for(int i=0; i < responseItems.size(); i++){
                    Repository repository = new Repository();
                    JSONObject item = (JSONObject) responseItems.get(i);
                    repository.setJenkinsFileUrl((String) item.get("url"));
                    repository.setRepositoryName((String) ((JSONObject) item.get("repository")).get("name"));
                    repository.setJenkinsFileContent(getFileContentsFromUrl(repository.getJenkinsFileUrl()));
                    repositoryList.add(repository);
                }
            }
        } catch (ParseException e) {
            PipelineAnalyzerException ex = new PipelineAnalyzerException("Error occurred while creating json object from response",e);
            logger.error("Error occurred while accessing json object in response", ex);
            throw ex;
        }

        return repositoryList;
    }

    private String getFileContentsFromUrl(String url) throws PipelineAnalyzerException {

        try {
            String content = performHTTPRequest(url);
            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(content.toString());

            if (json.containsKey("download_url")) {
                String downloadUrl = (String) json.get("download_url");

                logger.info("Getting file contents using download url " + downloadUrl);

                return performHTTPRequest(downloadUrl);

            } else {
                PipelineAnalyzerException ex = new PipelineAnalyzerException("Download url for Jenkins file unavailable");
                logger.fatal(ex);
                throw ex;
            }

        } catch (ParseException e) {
            PipelineAnalyzerException ex = new PipelineAnalyzerException("Exception occurred while parsing json response ",e);
            logger.fatal("Exception occurred while parsing json response", ex);
            throw ex;
        }

    }

    private String performHTTPRequest(String url) throws PipelineAnalyzerException {

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();

            HttpGet request = createRequestFromUrl(url);

            HttpResponse urlResponse = httpClient.execute(request);

            BufferedReader br  = new BufferedReader(new InputStreamReader(urlResponse.getEntity().getContent()));

            StringBuilder content = new StringBuilder();
            String line = "";
            while((line = br.readLine()) != null){

                content.append(line);
            }


            return content.toString();


        } catch (IOException e) {
            PipelineAnalyzerException ex = new PipelineAnalyzerException("Exception occurred while performing http request using url",e);
            logger.fatal("Exception occurred for http request using URL"+url, ex);
            throw ex;

        }

    }

}
