package com.uic.atse.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
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
                // Pipeline pipe = objectMapper.readValue(new File("D:\\UIC\\Sem 2\\CS540- Advanced Software Engineering\\Course Project\\GIT\\2\\jenkins.json"), Pipeline.class);
                Pipeline pipe = objectMapper.readValue(json, Pipeline.class);
                logger.info(pipe.toString());


                return pipe;
            }
            catch(Exception e){
                e.printStackTrace();
                return  p;
            }
    }

}
