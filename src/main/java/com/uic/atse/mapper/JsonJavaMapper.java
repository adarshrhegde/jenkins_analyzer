package com.uic.atse.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.model.Pipeline;
import org.apache.log4j.Logger;

import java.io.IOException;

public class JsonJavaMapper  {
    Logger logger = Logger.getLogger(JsonJavaMapper.class.getName());


    // creating representation of pipeline in form of class objects from json of jenkins file

    public Pipeline readJsonWithObjectMapper(String json) throws PipelineAnalyzerException{
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Pipeline pipeline = objectMapper.readValue(json, Pipeline.class);
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                logger.trace("Pipeline object" + pipeline.toString());
                return pipeline;
            }

            catch(IOException e){
                PipelineAnalyzerException ex = new PipelineAnalyzerException("Error occurred while creating pipeline object from json",e);
                logger.error("Error occurred while creating pipeline object from json", ex);
                return null;
            }

    }


}

