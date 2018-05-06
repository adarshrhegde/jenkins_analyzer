package com.uic.atse.utils;

import com.uic.atse.exception.PipelineAnalyzerException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class PipelineAnalyzerProperties extends Properties {

    Logger logger = Logger.getLogger(PipelineAnalyzerProperties.class.getName());

    private static PipelineAnalyzerProperties props;

    private String githubBaseUrl;

    private String githubPath;

    private String githubQueryString;

    private String githubSearchPath;

    private String githubSearchType;

    private String githubRef;

    private String githubUserAgent;

    private String githubAuthorization;

    private String jenkinsURL;

    @Override
    public synchronized void load(InputStream inStream) throws IOException {

        logger.info("Loading properties file ");
        super.load(inStream);

        this.githubAuthorization = (String) get("githubAuthorization");
        this.githubBaseUrl = (String) get("githubBaseUrl");
        this.githubPath = (String) get("githubPath");
        this.githubQueryString = (String) get("githubQueryString");
        this.githubRef = (String) get("githubRef");
        this.githubSearchPath = (String) get("githubSearchPath");
        this.githubUserAgent = (String) get("githubUserAgent");
        this.githubSearchType = (String) get("githubSearchType");
        this.jenkinsURL = (String) get("jenkinsURL");

    }

    private PipelineAnalyzerProperties() throws PipelineAnalyzerException {
        super();

        try {
            load(new FileInputStream(new File("resources/analyzer.properties")));
        } catch (IOException e) {
            PipelineAnalyzerException ex = new PipelineAnalyzerException("Properties file not found",e);
            logger.fatal("Exception occurred while loading properties ", ex);
            throw ex;

        }
    }

    /**
     * returns the singleton instance for the properties class
     */
    public static PipelineAnalyzerProperties getInstance() throws PipelineAnalyzerException {
        if(props == null)
            props = new PipelineAnalyzerProperties();

        return props;

    }

    public String getGithubBaseUrl() {
        return githubBaseUrl;
    }

    public String getGithubPath() {
        return githubPath;
    }

    public String getGithubQueryString() {
        return githubQueryString;
    }

    public String getGithubSearchPath() {
        return githubSearchPath;
    }

    public String getGithubSearchType() {
        return githubSearchType;
    }

    public String getGithubRef() {
        return githubRef;
    }

    public String getGithubUserAgent() {
        return githubUserAgent;
    }

    public String getGithubAuthorization() {
        return githubAuthorization;
    }

    public String getJenkinsURL() {
        return jenkinsURL;
    }

    @Override
    public String toString() {
        return "PipelineAnalyzerProperties{" +
                "githubBaseUrl='" + githubBaseUrl + '\'' +
                ", githubPath='" + githubPath + '\'' +
                ", githubQueryString='" + githubQueryString + '\'' +
                ", githubSearchPath='" + githubSearchPath + '\'' +
                ", githubSearchType='" + githubSearchType + '\'' +
                ", githubRef='" + githubRef + '\'' +
                ", githubUserAgent='" + githubUserAgent + '\'' +
                ", githubAuthorization='" + githubAuthorization + '\'' +
                ", jenkinsURL='" + jenkinsURL + '\'' +
                '}';
    }
}
