package com.uic.atse;

import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.impl.GithubRepositoryQuery;
import com.uic.atse.impl.RepositoryServiceImpl;
import com.uic.atse.model.QueryType;
import com.uic.atse.model.Repository;
import com.uic.atse.service.RepositoryQuery;
import com.uic.atse.service.RepositoryService;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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

            FileOutputStream fo = new FileOutputStream(new File("D:\\git\\adarsh_hegde_ashwani_khemani_srinath_kv_cp\\jenkins.txt"));
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


        } catch (PipelineAnalyzerException e) {
            logger.fatal("Exception occurred", e);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
