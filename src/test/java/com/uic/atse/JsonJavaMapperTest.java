package com.uic.atse;

import com.uic.atse.mapper.JsonJavaMapper;
import com.uic.atse.model.Agent;
import com.uic.atse.model.Agent_;
import com.uic.atse.model.Pipeline;
import org.junit.Test;

public class JsonJavaMapperTest {
    @Test
    public void readJsonWithObjectMapperTest() {
        String str="{\"agent\":{\"type\":\"any\"}}";
        JsonJavaMapper mapper = new JsonJavaMapper();
        Pipeline pipe=mapper.readJsonWithObjectMapper(str);
        Pipeline pipe2=new Pipeline();

        Agent_ a= new Agent_();
        a.setType("any");
        pipe2.setAgent(a);
        System.out.println("Pipe 2 "+pipe2.toString());
        System.out.println("Pipe 1 "+pipe.toString());

        System.out.println((pipe.equals(pipe2)));

    }
}
