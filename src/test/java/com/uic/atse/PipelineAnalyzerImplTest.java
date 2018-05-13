package com.uic.atse;

import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.impl.PipelineAnalyzerImpl;
import com.uic.atse.mapper.JsonJavaMapper;
import com.uic.atse.model.Pipeline;
import com.uic.atse.utils.PipelineAnalyzerProperties;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tests the PipelineAnalyzerImpl class
 */
public class PipelineAnalyzerImplTest {


    Logger logger = Logger.getLogger(PipelineAnalyzerImplTest.class.getName());

    /**
     * Gets a list of pipeline objects from json strings in file
     * @return
     */
    public List<Pipeline> getPipelineList(){

        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource("jenkins_json.txt").getFile());
        try {
            List<String> lines = FileUtils.readLines(file, "utf-8");
            JsonJavaMapper mapper = new JsonJavaMapper();

            List<Pipeline> pipelines = lines.stream().filter(line -> line.length() > 0)
                    .map(line-> {
                        try {
                            Pipeline pipeline = mapper.readJsonWithObjectMapper(line.substring(12,line.length()));
                            return pipeline;
                        } catch (PipelineAnalyzerException e) {
                            logger.error("Error occurred while converting following " +
                                    "json line to pipeline object" + line);
                            return null;
                        }

                    }).collect(Collectors.toList());

            return pipelines;

        } catch (IOException e) {
            logger.fatal("Exception occurred while constructing pipelines list");
            return null;
        }
    }

    /**
     * Tests the convertToJsonFile method
     * Creates a hashmap to test the method, finally json from generated file is compared to manually
     * generated json
     */
    @Test
    public void testCovertToJsonFile(){

        try {
            List<Pipeline> pipelines = getPipelineList();
            if(null == pipelines)
                Assert.fail("Constructed Pipelines list is null");

            PipelineAnalyzerImpl pipelineAnalyzer = new PipelineAnalyzerImpl(pipelines);

            Map<String,Integer> map = new HashMap<>();
            map.put("item1", 100);
            map.put("item2", 40);
            map.put("item3", 60);

            pipelineAnalyzer.convertToJsonFile("test", map);

            JSONObject object = new JSONObject("{\"item1\":100,\"item2\":40,\"item3\":60}");

            PipelineAnalyzerProperties properties = PipelineAnalyzerProperties.getInstance();

            String fileLocation = properties.getOutputDirectory() + "test.json";

            String fileContents = FileUtils.readFileToString(new File(fileLocation), "utf-8");

            JSONObject jsonObject = new JSONObject(fileContents);

            Assert.assertTrue(jsonObject.toString().equals(object.toString()));

        } catch (PipelineAnalyzerException | JSONException | IOException e) {
            Assert.fail("Exception occurred while testing convertToJsonFile");
        }

    }

    /**
     * Tests the frequentPostConditions method
     * Creates a PipelineAnalyzerImpl object and executes the method to be tested
     * The output file content is validated with the expected output
     */
    @Test
    public void testFrequentPostConditions(){

        List<Pipeline> pipelines = getPipelineList();
        if(null == pipelines)
            Assert.fail("Constructed Pipelines list is null");

        try {
            PipelineAnalyzerImpl pipelineAnalyzer = new PipelineAnalyzerImpl(pipelines);
            pipelineAnalyzer.frequentPostConditions();

            String expectedOutput = "{\"always\":6,\"unstable\":2,\"success\":11,\"failure\":11,\"changed\":2}";

            PipelineAnalyzerProperties properties = PipelineAnalyzerProperties.getInstance();

            String output = FileUtils.readFileToString(new File(properties.getOutputDirectory() + "frequentPostConditions.json"), "utf-8");

            Assert.assertEquals(expectedOutput, output);

        } catch (PipelineAnalyzerException | IOException e) {
            Assert.fail("Exception occurred in test case testFrequentPostConditions");

        }
    }

    /**
     * Tests the frequentAgentTypes method
     * Creates a PipelineAnalyzerImpl object and executes the method to be tested
     * The output file content is validated with the expected output
     */
    @Test
    public void testFrequentAgentTypes(){

        List<Pipeline> pipelines = getPipelineList();
        if(null == pipelines)
            Assert.fail("Constructed Pipelines list is null");

        try {
            PipelineAnalyzerImpl pipelineAnalyzer = new PipelineAnalyzerImpl(pipelines);
            pipelineAnalyzer.frequentAgentTypes();

            String expectedOutput = "{\"node\":2,\"label\":4,\"none\":1,\"any\":49,\"docker\":26,\"none_stage\":3}";

            PipelineAnalyzerProperties properties = PipelineAnalyzerProperties.getInstance();

            String output = FileUtils.readFileToString(new File(properties.getOutputDirectory() + "frequentAgentTypes.json"), "utf-8");

            Assert.assertEquals(expectedOutput, output);

        } catch (PipelineAnalyzerException | IOException e) {
            Assert.fail("Exception occurred in test case testFrequentPostConditions");

        }
    }

    /**
     * Tests the frequentStepTypes method
     * Creates a PipelineAnalyzerImpl object and executes the method to be tested
     * The output file content is validated with the expected output
     */
    @Test
    public void testFrequentStepTypes(){

        List<Pipeline> pipelines = getPipelineList();
        if(null == pipelines)
            Assert.fail("Constructed Pipelines list is null");

        try {
            PipelineAnalyzerImpl pipelineAnalyzer = new PipelineAnalyzerImpl(pipelines);
            pipelineAnalyzer.frequentStepTypes();

            String expectedOutput = "{\"git\":2,\"bat\":28,\"archiveArtifacts\":1,\"sh\":68,\"shell\":1,\"withMaven\":12,\"powershell\":1,\"echo\":81,\"nexusArtifactUploader\":1}";

            PipelineAnalyzerProperties properties = PipelineAnalyzerProperties.getInstance();

            String output = FileUtils.readFileToString(new File(properties.getOutputDirectory() + "frequentStepTypes.json"), "utf-8");

            Assert.assertEquals(expectedOutput, output);

        } catch (PipelineAnalyzerException | IOException e) {
            Assert.fail("Exception occurred in test case testFrequentPostConditions");

        }
    }

    /**
     * Tests the analyzeUserDefinedParameters method
     * Creates a PipelineAnalyzerImpl object and executes the method to be tested
     * The output file content is validated with the expected output
     */
    @Test
    public void testAnalyzeUserDefinedParameters(){

        List<Pipeline> pipelines = getPipelineList();
        if(null == pipelines)
            Assert.fail("Constructed Pipelines list is null");

        try {
            PipelineAnalyzerImpl pipelineAnalyzer = new PipelineAnalyzerImpl(pipelines);
            pipelineAnalyzer.analyzeUserDefinedParameters();

            String expectedOutput = "{\"no_userDefined\":85,\"has_userDefined\":3}";

            PipelineAnalyzerProperties properties = PipelineAnalyzerProperties.getInstance();

            String output = FileUtils.readFileToString(new File(properties.getOutputDirectory() + "analyzeUserDefinedParameters.json"), "utf-8");

            Assert.assertEquals(expectedOutput, output);

        } catch (PipelineAnalyzerException | IOException e) {
            Assert.fail("Exception occurred in test case testFrequentPostConditions");

        }

    }


    /**
     * Tests the frequentEnvVarTypes method
     * Creates a PipelineAnalyzerImpl object and executes the method to be tested
     * The output file content is validated with the expected output
     */
    @Test
    public void testFrequentEnvVarTypes(){

        List<Pipeline> pipelines = getPipelineList();
        if(null == pipelines)
            Assert.fail("Constructed Pipelines list is null");

        try {
            PipelineAnalyzerImpl pipelineAnalyzer = new PipelineAnalyzerImpl(pipelines);
            pipelineAnalyzer.frequentEnvVarTypes();

            String expectedOutput = "{\"CI\":1}";

            PipelineAnalyzerProperties properties = PipelineAnalyzerProperties.getInstance();

            String output = FileUtils.readFileToString(new File(properties.getOutputDirectory() + "frequentEnvVarTypes.json"), "utf-8");

            Assert.assertEquals(expectedOutput, output);

        } catch (PipelineAnalyzerException | IOException e) {
            Assert.fail("Exception occurred in test case testFrequentPostConditions");

        }

    }


    /**
     * Tests the frequentUserDefinedParameters method
     * Creates a PipelineAnalyzerImpl object and executes the method to be tested
     * The output file content is validated with the expected output
     */
    @Test
    public void testFrequentUserDefinedParameters(){

        List<Pipeline> pipelines = getPipelineList();
        if(null == pipelines)
            Assert.fail("Constructed Pipelines list is null");

        try {
            PipelineAnalyzerImpl pipelineAnalyzer = new PipelineAnalyzerImpl(pipelines);
            pipelineAnalyzer.frequentEnvVarTypes();

            String expectedOutput = "{\"Username\":2,\"Environment\":1,\"tomcat_dev\":1,\"tomcat_prod\":1}";

            PipelineAnalyzerProperties properties = PipelineAnalyzerProperties.getInstance();

            String output = FileUtils.readFileToString(new File(properties.getOutputDirectory() + "frequentUserDefinedParameters.json"), "utf-8");

            Assert.assertEquals(expectedOutput, output);

        } catch (PipelineAnalyzerException | IOException e) {
            Assert.fail("Exception occurred in test case testFrequentPostConditions");

        }
    }

    /**
     * Tests the mostUsedTools method
     * Creates a PipelineAnalyzerImpl object and executes the method to be tested
     * The output file content is validated with the expected output
     */
    @Test
    public void testMostUsedTools(){

        List<Pipeline> pipelines = getPipelineList();
        if(null == pipelines)
            Assert.fail("Constructed Pipelines list is null");

        try {
            PipelineAnalyzerImpl pipelineAnalyzer = new PipelineAnalyzerImpl(pipelines);
            pipelineAnalyzer.mostUsedTools();

            String expectedOutput = "{\"jdk\":6,\"gradle\":0,\"maven\":7}";

            PipelineAnalyzerProperties properties = PipelineAnalyzerProperties.getInstance();

            String output = FileUtils.readFileToString(new File(properties.getOutputDirectory() + "mostUsedTools.json"), "utf-8");

            Assert.assertEquals(expectedOutput, output);

        } catch (PipelineAnalyzerException | IOException e) {
            Assert.fail("Exception occurred in test case testFrequentPostConditions");

        }
    }

    /**
     * Tests the leastUsedTools method
     * Creates a PipelineAnalyzerImpl object and executes the method to be tested
     * The output file content is validated with the expected output
     */
    @Test
    public void leastUsedTools(){
        List<Pipeline> pipelines = getPipelineList();
        if(null == pipelines)
            Assert.fail("Constructed Pipelines list is null");

        try {
            PipelineAnalyzerImpl pipelineAnalyzer = new PipelineAnalyzerImpl(pipelines);

            String expectedOutput = "maven & jdk";

            Assert.assertEquals(expectedOutput, pipelineAnalyzer.leastUsedTools());

        } catch (PipelineAnalyzerException e) {
            Assert.fail("Exception occurred in test case testFrequentPostConditions");

        }

    }



}
