package com.uic.atse;

import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.impl.GithubRepositoryQuery;
import com.uic.atse.impl.RepositoryServiceImpl;
import com.uic.atse.mapper.JsonJavaMapper;
import com.uic.atse.model.Example;
import com.uic.atse.model.Pipeline;
import com.uic.atse.model.QueryType;
import com.uic.atse.model.Repository;
import com.uic.atse.service.RepositoryQuery;
import com.uic.atse.service.RepositoryService;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args){


        logger.info("Welcome to the project");

        try {

            RepositoryQuery query = new GithubRepositoryQuery(QueryType.jenkinsFile);

            // injecting the dependency for the repository query type
            RepositoryService repositoryService = new RepositoryServiceImpl(query);

            // retrieve the results for the query
            //System.out.println(.get(10));
            List<Repository> repositories = repositoryService.getQueryResult();

            FileOutputStream fo = new FileOutputStream(new File("D:\\UIC\\Sem 2\\CS540- Advanced Software Engineering\\Course Project\\jenkins.txt"));
            System.out.println(repositories.size());

            /*repositories.stream().forEach((repo) -> {
                        System.out.println(repo.getJenkinsFileContent());
                        System.out.println();
                        System.out.println();
                    });*/
            int d=0;
            int s=0;
            repositories.stream().forEach((repo) -> {
                try {

                    if(repo.getJenkinsFileContent().startsWith("pipeline")){
                        System.out.println("Declarative Pipeline");
                        fo.write((repo.getJson()+"\n").getBytes());
                        //fo.write((repo.getJenkinsFileContent()).getBytes());
                    } else {
                        System.out.println("Scripting Pipeline");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            fo.flush();
            fo.close();

            JsonJavaMapper json1 = new JsonJavaMapper();

            List<Pipeline> pipelines=repositories.stream().filter(repository->null!=repository.getJson()).map((repo)-> {
                Pipeline pipe= null;
                try {
                    System.out.println(repo.getJson().toString());
                    pipe = json1.readJsonWithObjectMapper(repo.getJson().toString());
                } catch (Exception e) {
                    e.printStackTrace();

                }
                System.out.println(pipe.toString());
                System.out.println("Stage= "+pipe.getStages());
                return pipe;
            }).collect(Collectors.toList());

            //call();


        } catch (PipelineAnalyzerException e) {
            logger.fatal("Exception occurred", e);
        } catch (IOException e) {
            e.printStackTrace();
        }

            //call();




    }

    public static void call(){
        JsonJavaMapper json1 = new JsonJavaMapper();
        try {

            Pipeline pipe = json1.readJsonWithObjectMapper("");
            System.out.println((pipe.toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
