package com.uic.atse.impl;

import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.mapper.JsonJavaMapper;
import com.uic.atse.model.Repository;
import com.uic.atse.service.RepositoryQuery;
import com.uic.atse.service.RepositoryService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

            List<StringBuilder> content = new ArrayList<>();

            // handles pagination in github developer api
            int i=1;
            boolean isAvailable = true;

            do {
                repositoryQuery.setPage(i);
                i++;
                HttpClient client = HttpClientBuilder.create().build();
                HttpGet request = repositoryQuery.createRequest();
                HttpResponse response = client.execute(request);
                logger.info("Status code for response " + response.getStatusLine().getStatusCode());

                BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuilder queryContent = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    queryContent.append(line);
                }

                logger.trace("Response " + queryContent);

                if (queryContent.toString().equals("")) {
                    logger.info("Empty response to query");
                    isAvailable = false;
                }
                content.add(queryContent);
            } while (isAvailable && i < 4);

            return repositoryQuery.convertResponse(content);

        } catch (IOException e) {

            PipelineAnalyzerException ex = new PipelineAnalyzerException("Http request to repository failed");
            logger.fatal("Exception occured while performing http request", ex);
            throw ex;
        }



    }




}
