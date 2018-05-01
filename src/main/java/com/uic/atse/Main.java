package com.uic.atse;

import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.impl.GithubRepositoryQuery;
import com.uic.atse.impl.RepositoryServiceImpl;
import com.uic.atse.model.QueryType;
import com.uic.atse.service.RepositoryQuery;
import com.uic.atse.service.RepositoryService;
import org.apache.log4j.Logger;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args){

        logger.info("Welcome to the project");

        try {

            RepositoryQuery query = new GithubRepositoryQuery(QueryType.jenkinsFile);

            // injecting the dependency for the repository query type
            RepositoryService repositoryService = new RepositoryServiceImpl(query);

            // retrieve the results for the query
            System.out.println(repositoryService.getQueryResult().get(10));

        } catch (PipelineAnalyzerException e) {
            logger.fatal("Exception occurred", e);
        }


    }


}
