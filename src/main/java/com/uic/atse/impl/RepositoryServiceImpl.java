package com.uic.atse.impl;

import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.model.Repository;
import com.uic.atse.service.RepositoryQuery;
import com.uic.atse.service.RepositoryService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class RepositoryServiceImpl implements RepositoryService {

    Logger logger = Logger.getLogger(RepositoryServiceImpl.class.getName());

    private RepositoryQuery repositoryQuery;

    public RepositoryServiceImpl(RepositoryQuery repositoryQuery){
        this.repositoryQuery = repositoryQuery;

    }

    @Override
    public List<Repository> getQueryResult() throws PipelineAnalyzerException {

        try {

            logger.info("Performing http request to get repositories for query");
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = repositoryQuery.createRequest();
            HttpResponse response = client.execute(request);
            logger.info("Status code for response " + response.getStatusLine().getStatusCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder content = new StringBuilder();
            String line = "";
            while((line = br.readLine()) != null){
                content.append(line);
            }

            logger.info("Response "+ content);

            if(content.equals("")){
                logger.info("Empty response to query");
                return null;
            }
            return repositoryQuery.convertResponse(content);

        } catch (IOException e) {

            PipelineAnalyzerException ex = new PipelineAnalyzerException("Http request to repository failed");
            logger.fatal("Exception occured while performing http request", ex);
            throw ex;
        }

    }




}
