package com.uic.atse;

import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.impl.GithubRepositoryQuery;
import com.uic.atse.impl.RepositoryServiceImpl;
import com.uic.atse.mapper.JsonJavaMapper;
import com.uic.atse.model.Pipeline;
import com.uic.atse.model.QueryType;
import com.uic.atse.model.Repository;
import com.uic.atse.service.RepositoryQuery;
import com.uic.atse.service.RepositoryService;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args){


        logger.info("Welcome to the project");

        try {

            RepositoryQuery query = new GithubRepositoryQuery(QueryType.jenkinsFile);

            // injecting the dependency for github repository query type into RepositoryServiceImpl
            RepositoryService repositoryService = new RepositoryServiceImpl(query);

            // retrieve the jenkins file using the query
            List<Repository> repositories = repositoryService.getQueryResult();

            JsonJavaMapper json1 = new JsonJavaMapper();

            // creating representation of pipeline in form of class objects
            List<Pipeline> pipelines=repositories.stream()
                    .filter(repository->null!=repository.getJson()).map((repo)-> {
                Pipeline pipe= null;
                try {
                    logger.trace("JSON String >>" + repo.getJson().toString());
                    pipe = json1.readJsonWithObjectMapper(repo.getJson().toString());
                } catch (Exception e) {
                    e.printStackTrace();

                }
                return pipe;
            }).filter(pipeline->null!=pipeline).collect(Collectors.toList());

            System.out.println("# of pipelines"+pipelines.size());
        } catch (PipelineAnalyzerException e) {
            logger.fatal("Exception occurred", e);
        }

    }


}
