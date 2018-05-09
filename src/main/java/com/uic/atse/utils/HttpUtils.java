package com.uic.atse.utils;

import com.uic.atse.exception.PipelineAnalyzerException;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for various utility methods
 */
public class HttpUtils {

    static Logger logger = Logger.getLogger(HttpUtils.class.getName());


    /**
     * Hit the Jenkins service to convert the jenkins file to json representation
     * @param fileData
     * @return
     */
    public static JSONObject getJsonFromJenkins(String fileData) throws PipelineAnalyzerException {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            PipelineAnalyzerProperties properties = PipelineAnalyzerProperties.getInstance();

            HttpEntity httpEntity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addBinaryBody("jenkinsfile", fileData.getBytes())
                    .build();

            HttpUriRequest request = RequestBuilder
                    .post(properties.getJenkinsURL() + "pipeline-model-converter/toJson")
                    .setEntity(httpEntity).build();
            System.out.println("Executing request " + request.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody = httpclient.execute(request, responseHandler);
            JSONParser parser = new org.json.simple.parser.JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(responseBody);
            JSONObject response = (JSONObject) ((JSONObject) jsonResponse.get("data")).get("json");
            if(null!=response){
                JSONObject pipeline= (JSONObject)response.get("pipeline");
                return pipeline;
            }
            return null;


        } catch (IOException | ParseException e) {
            PipelineAnalyzerException ex = new PipelineAnalyzerException("Exception occurred while requesting jenkins service for json", e);
            logger.error(ex);

            return null;
        }

    }


}
