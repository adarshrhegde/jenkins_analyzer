package com.uic.atse.impl;

/*
 Class for analyzing pipeline objects
*/

import com.google.gson.Gson;
import com.uic.atse.model.*;
import org.apache.commons.io.FileUtils;
import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.utils.PipelineAnalyzerProperties;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PipelineAnalyzerImpl {

    // List of pipeline objects

    List<Pipeline> pipelines;
    int maven=0,jdk=0,gradle=0;


    Logger logger = Logger.getLogger(PipelineAnalyzerImpl.class);

    PipelineAnalyzerProperties properties;

    public PipelineAnalyzerImpl(List<Pipeline> pipelines) throws PipelineAnalyzerException {
        this.pipelines=pipelines;
        properties = PipelineAnalyzerProperties.getInstance();

    }

    /**
     *  Perform analysis on pipeline objects
     */
    public void execute() {
        frequentPostConditions();
        frequentAgentTypes();
        frequentStepTypes();
        frequentEnvVarTypes();
        frequentUserDefinedParameters();
        analyzeUserDefinedParameters();

        /** Q1 Most used Tools in Jenkins pipelines **/
        System.out.println("Most Used Tools  in Jenkins pipelines : "+mostUsedTools());

        /** Q2 Least used Tools in Jenkins Pipelines **/
        System.out.println("Least used tool in Jenkins pipelines : "+leastUsedTools());

        /** Q3 Find relation between triggers and number of stages */
        System.out.println("Relation between Triggers and steps: "+triggerStagesCorelation());

    }

    /**
     * What are the most frequent post-condition blocks in the post section within jenkins pipelines?
     */
    public void frequentPostConditions() {
        HashMap<String, Integer> condition_freq = new HashMap<String, Integer>();
        System.out.println("getting FrequentPostConditionBlocks");

        // getting post conditions blocks at pipeline level
        for(Pipeline pipeline : pipelines)
        {
            if(null != pipeline && null != pipeline.getPost() && null != pipeline.getPost().getConditions()){
                List<Condition_> conditions = pipeline.getPost().getConditions();
                for (Condition_ condition : conditions){
                    Integer freq = condition_freq.get(condition.getCondition());
                    condition_freq.put(condition.getCondition(), freq==null ? 1 : freq + 1);
                }
            }
        }
        // getting post conditions blocks at pipeline-stage level
        for (Pipeline pipeline : pipelines) {
            if (null != pipeline && null != pipeline.getStages()) {
                for (Stage stage : pipeline.getStages()) {
                    if(null !=stage && null != stage.getPost() && null!=stage.getPost().getConditions()) {
                        List<Condition_> conditions = stage.getPost().getConditions();
                        for (Condition_ condition : conditions) {
                            Integer freq = condition_freq.get(condition.getCondition());
                            condition_freq.put(condition.getCondition(), freq == null ? 1 : freq + 1);
                        }
                    }
                }
            }
        }
        System.out.print("Most frequent post condition blocks"+condition_freq.toString());
        convertToJsonFile("frequentPostConditions",condition_freq);

    }

    /**
     *What are the most frequent agent type in the stage section within jenkins pipelines?
      */
    public void frequentAgentTypes() {
        HashMap<String, Integer> agent_freq = new HashMap<String, Integer>();
        System.out.println("getting FrequentAgentTypes");

        // getting agent blocks at pipeline level
        for(Pipeline pipeline : pipelines)
        {
            if(null != pipeline && null != pipeline.getAgent() && null != pipeline.getAgent().getType()){
                Integer freq = agent_freq.get(pipeline.getAgent().getType());
                agent_freq.put(pipeline.getAgent().getType(), freq==null ? 1 : freq + 1);
            }
        }
        // getting agent blocks at pipeline-stage level
        for (Pipeline pipeline : pipelines) {
            if (null != pipeline && null != pipeline.getStages()) {
                for (Stage stage : pipeline.getStages()) {
                    if (null != stage && null != stage.getAgent() && null != stage.getAgent().getType()) {
                        Integer freq_stage = agent_freq.get(pipeline.getAgent().getType()+"_stage");
                        agent_freq.put(pipeline.getAgent().getType()+"_stage", freq_stage == null ? 1 : freq_stage + 1);
                    }
                }
            }
        }
        System.out.println("Agent types and their frequencies across different pipelines "+agent_freq.toString());
        convertToJsonFile("frequentAgentTypes",agent_freq);

    }

    /**
     *     What are the most frequent steps type in the stage section within jenkins pipelines ?
     */
    public void frequentStepTypes() {
        HashMap<String, Integer> step_freq = new HashMap<String, Integer>();
        System.out.println("getting FrequentStepTypes");

        // getting agent blocks at pipeline-stage level
        for (Pipeline pipeline : pipelines) {
            if (null != pipeline && null != pipeline.getStages()) {
                for (Stage stage : pipeline.getStages()) {
                    if (null != stage && null != stage.getBranches()) {
                        for (Branch branch : stage.getBranches()) {
                            if (null != branch && null != branch.getSteps()) {
                                for (Step step : branch.getSteps()) {
                                    Integer freq = step_freq.get(step.getName());
                                    step_freq.put(step.getName(), freq == null ? 1 : freq + 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Steps types and their frequencies across different pipelines " + step_freq.toString());
        convertToJsonFile("frequentStepTypes", step_freq);

    }
    /**
    * Analyze the number of projects that have user-defined parameters
    */
    public void analyzeUserDefinedParameters() {

        logger.info("Analyze the number of projects having/not having user-defined parameters");

        float totalPipelines = pipelines.size();

        float pipelinesWithParameters = pipelines.stream().filter(pipeline -> null != pipeline.getParameters()).count();

        JSONObject json = new JSONObject();
        try {
            json.put("has_userDefined", pipelinesWithParameters);
            json.put("no_userDefined", totalPipelines-pipelinesWithParameters);
            FileUtils.writeStringToFile(new File(properties.getOutputDirectory() +"analyzeUserDefinedParameters.json"),json.toString(),"UTF-8");


        } catch (JSONException e) {
            PipelineAnalyzerException ex = new PipelineAnalyzerException("Exception occurred while creating json", e);
            logger.error(ex);

        } catch (IOException e) {
            PipelineAnalyzerException ex = new PipelineAnalyzerException("Exception occurred while writing json output to file", e);
            logger.error(ex);

        }

    }

    /**
     *    What are the most frequent steps type in the environment variables within jenkins pipelines ?
     */
    public void frequentEnvVarTypes() {
        HashMap<String, Integer> env_freq = new HashMap<String, Integer>();
        System.out.println("getting frequentEnvVarTypes");

        // getting post environment types at pipeline level
        for(Pipeline pipeline : pipelines) {
            if(null != pipeline && null != pipeline.getEnvironment()) {
                for (Environment env : pipeline.getEnvironment()) {
                    Integer freq = env_freq.get(env.getKey());
                    env_freq.put(env.getKey(), freq == null ? 1 : freq + 1);
                }
            }
        }

        // getting post environment types at pipeline-stage level
        for(Pipeline pipeline : pipelines) {
            if (null != pipeline && null != pipeline.getStages()) {
                for (Stage stage : pipeline.getStages()) {
                    if (null != stage && null != stage.getEnvironment()) {
                        for (Environment env : stage.getEnvironment()) {
                            Integer freq = env_freq.get(env.getKey());
                            env_freq.put(env.getKey(), freq == null ? 1 : freq + 1);
                        }
                    }
                }
            }
        }
        System.out.print("Most frequent environment variables"+env_freq.toString());
        convertToJsonFile("frequentEnvVarTypes", env_freq);
        logger.info("Result>>" + env_freq);
    }

    /**
     * Analyze the most frequent User-Defined Parameters in a jenkins file
     */
    public void frequentUserDefinedParameters() {
        logger.info("Analyzing the most frequent user-defined parameters in jenkins files");

        Map<String, Integer> paramNames = new HashMap<>();

        pipelines.stream().filter(pipeline -> null != pipeline.getParameters()).forEach(pipeline -> {
            List<Parameter> parameters = pipeline.getParameters().getParameters();

            parameters.stream().filter(parameter -> null != parameter).forEach(parameter -> {

                parameter.getArguments().stream()
                        .filter(argument -> null!= argument && argument.getKey().equals("name"))
                        .forEach(argument -> {

                            int count = paramNames.containsKey(argument.getValue().getValue())
                                    ? paramNames.get(argument.getValue().getValue()).intValue() : 0;

                            paramNames.put(argument.getValue().getValue(), count + 1);

                        });
            });

        });

        logger.info("Result>>" + paramNames);

        this.<String,Integer>convertToJsonFile("frequentUserDefinedParameters", paramNames);

    }

    /**
     * convert map to json and write to file
     * @param filename
     * @param result
     * @param <T>
     * @param <S>
     */
    public <T,S> void convertToJsonFile(String filename , Map<T,S> result){
        Gson gson = new Gson();
        String outputJson = gson.toJson(result);
        try {
            FileUtils.writeStringToFile(new File(properties.getOutputDirectory() + filename+".json"),outputJson,"UTF-8");
        } catch (IOException e) {
            PipelineAnalyzerException ex = new PipelineAnalyzerException("Exception while writing json result to file");
            logger.error(ex);
        }
    }


    /**
     * Finds the most used tool in jenkins pipeline amongst Maven, Gradle and Jdk   **/
    public String mostUsedTools(){
        String MostUsedTool=new String();

        System.out.println("Size main:"+pipelines.size());
        for(int i=0;i<pipelines.size();i++) {
            if (pipelines.get(i).getTools() != null){
                System.out.println(pipelines.get(i).getTools().toString());
                List<Tool> toolList = pipelines.get(i).getTools();
                if (!toolList.isEmpty()) {
                    for (Tool tools : toolList) {
                        if(tools.getKey()!=null){
                            if(tools.getKey().toString().equals("maven"))
                                maven++;
                            else if(tools.getKey().toString().equals("jdk"))
                                jdk++;
                            else if(tools.getKey().toString().equals("gradle"))
                                gradle++;
                        }
                    };
                }
            }
        }


        if(maven>gradle && maven>jdk)
            MostUsedTool="maven";
        else if(gradle>maven && gradle>jdk)
            MostUsedTool="gradle";
        else if(jdk>maven && jdk>gradle)
            MostUsedTool="jdk";
        else if(maven==jdk)
            MostUsedTool="maven & jdk";
        else if(maven==gradle)
            MostUsedTool="gradle & maven";
        else if(jdk==gradle)
            MostUsedTool="jdk & gradle";

        create_json();
        return MostUsedTool;
    }


    public String leastUsedTools(){
        String leastUsedTool=new String();

        if(maven<gradle && maven<jdk)
            leastUsedTool="maven";
        else if(jdk<gradle && jdk<maven)
            leastUsedTool="jdk";
        else if(gradle<jdk && gradle<maven)
            leastUsedTool="gradle";
        else if(maven==jdk)
            leastUsedTool="maven & jdk";
        else if(maven==gradle)
            leastUsedTool="gradle & maven";
        else if(jdk==gradle)
            leastUsedTool="jdk & gradle";

        return leastUsedTool;
    }

    /** Create json for tools**/
    public  void create_json(){
        try {
            JSONObject obj = new JSONObject();
            obj.put("maven", maven);
            obj.put("jdk", jdk);
            obj.put("gradle", gradle);

            String jsonText = obj.toString();
            System.out.print(jsonText);

            FileUtils.writeStringToFile(new File(properties.getOutputDirectory()+ "Most Used Tools.json"),jsonText,"utf-8");
        }
        catch(IOException i)
        { PipelineAnalyzerException ex = new PipelineAnalyzerException("Exception while printing json result to file");
            logger.error(ex);

        }
        catch(JSONException j){
            PipelineAnalyzerException ex = new PipelineAnalyzerException("JSONException while writing json result to file");
            logger.error(ex);

        }


    }

    /**Create a method that finds corealtion between triggers and number of stages*/
    public String triggerStagesCorelation(){


        for(Pipeline pipeline:pipelines){
            System.out.println("Inside TriggerStagesCorelation:"+pipeline.toString());
            if(pipeline!=null) {
                Triggers triger = pipeline.getTriggers();
                if (triger != null){
                    List<Trigger> triggerList = pipeline.getTriggers().getTriggers();
                    for (Trigger trigger : triggerList) {
                        if (trigger != null) {
                            String name = trigger.getName().toString();
                            System.out.println("Trigger name: " + name);

                            List<Argument________> listArgs = trigger.getArguments();
                            if (listArgs != null) {
                                for (Argument________ args : listArgs) {
                                    String value = args.getValue().toString();
                                    System.out.println("Trigger args Value: " + value);


                                }
                                ;
                            }
                        }
                    }
                    ;
                }
            }
        };

        return "1";
    }

}
