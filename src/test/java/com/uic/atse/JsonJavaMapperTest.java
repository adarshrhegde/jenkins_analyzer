package com.uic.atse;

import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.mapper.JsonJavaMapper;
import com.uic.atse.model.*;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test the object mapper
 */
public class JsonJavaMapperTest {

    /**
     * Test for the method readJsonWithObjectMapper
     */
    @Test
    public void readJsonWithObjectMapperTest() {
        Pipeline pipeline = new Pipeline();

        Agent agent = new Agent();
        agent.setType("any");
        pipeline.setAgent(agent);

        List<Stage> stages = new ArrayList<>();
        Stage stage1 = new Stage();
        stage1.setName("Build");
        List<Branch> branches = new ArrayList<>();

        Branch branch1 = new Branch();
        branch1.setName("default");

        List<Step> steps = new ArrayList<>();
        Step step = new Step();
        step.setName("echo");

        List<Argument> arguments = new ArrayList<>();
        Argument argument = new Argument();
        argument.setKey("message");
        Value value1 = new Value();
        value1.setIsLiteral(true);
        value1.setValue("Building..");
        argument.setValue(value1);
        arguments.add(argument);

        step.setArguments(arguments);
        steps.add(step);
        branch1.setSteps(steps);
        branches.add(branch1);
        stage1.setBranches(branches);
        stages.add(stage1);

        Stage stage2 = new Stage();
        stage2.setName("Deploy");
        List<Branch> branches2 = new ArrayList<>();
        Branch branch2 = new Branch();
        branch2.setName("default");

        List<Step> steps2 = new ArrayList<>();
        Step step2 = new Step();
        step2.setName("echo");
        List<Argument> arguments2 = new ArrayList<>();
        Argument argument2 = new Argument();
        Value value2 = new Value();
        value2.setIsLiteral(true);
        value2.setValue("Deploying....");
        argument2.setValue(value2);
        argument2.setKey("message");
        arguments2.add(argument2);

        step2.setArguments(arguments2);
        steps2.add(step2);
        branch2.setSteps(steps2);
        branches2.add(branch2);
        stage2.setBranches(branches2);

        stages.add(stage2);
        pipeline.setStages(stages);

        String json = null;

        try {
            json = FileUtils.readFileToString(new File("D:\\jenkinsfilesample\\jenkins.json"),"utf-8");

            JsonJavaMapper jsonMapper = new JsonJavaMapper();

            System.out.println(json);
            Pipeline pipeline2 = jsonMapper.readJsonWithObjectMapper(json);
            System.out.println(pipeline);
            System.out.println(pipeline2);
            Assert.assertTrue(pipeline.equals(pipeline2));

        } catch (IOException | PipelineAnalyzerException e) {
            Assert.fail("Exception occurred while testing the json java object mapper ");
        }


    }
}
