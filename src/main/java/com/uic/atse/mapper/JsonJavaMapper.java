package com.uic.atse.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.uic.atse.model.Example;
import com.uic.atse.model.Pipeline;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import java.io.File;
import java.io.IOException;

public class JsonJavaMapper {
    Logger logger = Logger.getLogger(JsonJavaMapper.class.getName());

    public Pipeline readJsonWithObjectMapper(String json) {
        Pipeline p=new Pipeline();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Pipeline pipe = objectMapper.readValue(new File("D:\\jenkinsfilesample\\jenkins.json"), Pipeline.class);
                //Pipeline pipe = objectMapper.readValue(json, Pipeline.class);
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                logger.info(pipe.toString());


                return pipe;
            }
            catch(Exception e){
                e.printStackTrace();
                return  p;
            }
    }


    public static void main(String[] args){

        JsonJavaMapper jsonJavaMapper = new JsonJavaMapper();
        jsonJavaMapper.readJsonWithObjectMapper("");
    }
}

