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


import javax.lang.model.element.Name;
import java.io.File;
import java.io.IOException;
import java.util.*;

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
        frequentWhenConditions();
        mostUsedTools();
        leastUsedTools();
        triggerStagesCorrelation();
        frequentStageTypes();
        commonCommandsInSteps();
        frequentAgentArgumentsTypes();
        stagesStepsCorrelation();

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
    public void triggerStagesCorrelation() {
        List<Integer> triggerCount = new ArrayList<Integer>();
        List<Integer> stagesCount = new ArrayList<Integer>();
        double similarity_score;
        System.out.println("triggerStagesCorelation");


        for (Pipeline pipeline : pipelines) {
            if (pipeline != null) {
                List<Stage> stageList = pipeline.getStages();
                Triggers trigger = pipeline.getTriggers();
                if (trigger != null) {
                    List<Trigger> triggerList = pipeline.getTriggers().getTriggers();
                    triggerCount.add(new Integer(triggerList.size()));
                }
                else{
                    triggerCount.add(new Integer(0));
                }
                if (null != stageList) {
                    stagesCount.add(new Integer(stageList.size()));
                }
            }
        }
        System.out.println(triggerCount.toString());
        System.out.println(stagesCount.toString());

        similarity_score=getCorrelationCoefficient(stagesCount,triggerCount);


    }

    /**
     * Find the most frequent when conditions types
     */
    public void frequentWhenConditions() {
        logger.info("Analyzing the most frequent when conditions in jenkins files");
        Map<String, Integer> whenCount = new HashMap<>();

        // getting when conditions at pipeline-stage level
        for (Pipeline pipeline : pipelines) {
            if (null != pipeline && null != pipeline.getStages()) {
                for (Stage stage : pipeline.getStages()) {
                    System.out.println(stage.toString());
                    if (null != stage && null != stage.getWhen() && null != stage.getWhen().getConditions()) {
                        List<Condition> conditions = stage.getWhen().getConditions();
                        for (Condition condition : conditions) {
                            Integer freq = whenCount.get(condition.getName());
                            whenCount.put(condition.getName(), freq == null ? 1 : freq + 1);
                        }
                    }
                }
            }
        }
        System.out.print("Frequent when condition types " + whenCount.toString());
        convertToJsonFile("frequentWhenConditions", whenCount);
    }

    /**
     *  Find the most common used stages in a pipeline
     */
    public void frequentStageTypes(){
        logger.info("Analyzing the most common stages in jenkins files");

        // getting stages at pipeline level
        Map<String, Integer> stageCount = new HashMap<>();

        for (Pipeline pipeline : pipelines) {
            if (null != pipeline && null != pipeline.getStages()) {
                for (Stage stage : pipeline.getStages()) {
                    if (null != stage && null != stage.getName()) {
                        Integer freq = stageCount.get(stage.getName());
                        stageCount.put(stage.getName(), freq == null ? 1 : freq + 1);
                    }
                }
            }
        }
        System.out.println("Frequent when condition types " + stageCount.toString());
        convertToJsonFile("frequentStageTypes", stageCount);

    }

    /**
     *  Find the most common used types of agent arguments
     */
    public void frequentAgentArgumentsTypes(){
        logger.info("Analyzing the most common agent argument type in jenkins files");
        HashMap<String, Integer> agentArgumentCount = new HashMap<String, Integer>();
        System.out.println("getting frequentAgentArgumentsTypes");

        // getting agent blocks at pipeline level
        for(Pipeline pipeline : pipelines)
        {
            if(null != pipeline && null != pipeline.getAgent() && null != pipeline.getAgent().getArguments()){
                for (Argument arg : pipeline.getAgent().getArguments()){
                    Integer freq = agentArgumentCount.get(arg.getValue().getValue());
                    agentArgumentCount.put(arg.getValue().getValue(), freq==null ? 1 : freq + 1);
                }
            }
        }
        // getting agent blocks at pipeline-stage level
        for (Pipeline pipeline : pipelines) {
            if (null != pipeline && null != pipeline.getStages()) {
                for (Stage stage : pipeline.getStages()) {
                    if (null != stage && null != stage.getAgent() && null != stage.getAgent().getArguments()) {
                        for (Argument arg : stage.getAgent().getArguments()){
                            Integer freq = agentArgumentCount.get(arg.getValue().getValue());
                            agentArgumentCount.put(arg.getValue().getValue(), freq==null ? 1 : freq + 1);
                        }
                    }
                }
            }
        }
        System.out.println("Most common Agent Argument Type across different pipelines "+agentArgumentCount.toString());
        convertToJsonFile("frequentAgentArgumentsTypes",agentArgumentCount);

    }

    /**
     * Finding the correlation between the number of stages and steps in a pipeline
     * @return
     */
    public double stagesStepsCorrelation() {

        logger.info("Find the correlation between number of stages and steps in jenkins files");
        System.out.println("getting stagesStepsCorrelation");

        List<Integer> stagesCount = new ArrayList<>();
        List<Integer> stepsCount = new ArrayList<>();
        double corrCoefficient = 0.0;

        // getting stages and steps count at pipeline level

        for (Pipeline pipeline : pipelines) {
            if (null != pipeline && null != pipeline.getStages()) {
                int numSteps = 0;
                for (Stage stage : pipeline.getStages()) {
                    if (null != stage && null != stage.getBranches()) {
                        for (Branch branch : stage.getBranches()) {
                            if (null != branch && null != branch.getSteps()) {
                                numSteps = numSteps + new Integer(branch.getSteps().size());
                            } else {
                                numSteps = numSteps + new Integer(0);
                            }
                        }
                    }
                }
                stepsCount.add(numSteps);
                if (null != pipeline.getStages()) {
                    stagesCount.add(new Integer(pipeline.getStages().size()));
                } else {
                    stagesCount.add(new Integer(0));
                }
            }
        }
        corrCoefficient=getCorrelationCoefficient(stagesCount,stepsCount);
        System.out.println("stagesStepsCorrelation "+corrCoefficient);
        return 0;
    }


    /** Calculates mean of the passed List<Integer>
     * */
    public double getMean(List<Integer> list){
        double mean=0;
        double temp=0;
        for (int i=0;i<list.size();i++){
            temp=list.get(i)+temp;
        }
        mean=temp/list.size();
        return mean;
    }


    /**Find the co-relation coefficient for two integer lists*/
    public double getCorrelationCoefficient(List<Integer> list1,List<Integer> list2){

        double stagesMean=getMean(list1);
        double triggerMean=getMean(list2);
        double x=0,y=0,num=0,sumX=0,sumY=0;
        double corrCoefficient=0;
        for(int i=0;i<list1.size();i++)
        {
            x=(list1.get(i)-stagesMean);
            y=(list2.get(i)-triggerMean);
            num=(x*y)+num;
            sumX=sumX+(x*x);
            sumY=sumY+(y*y);

        }
        corrCoefficient=num/((Math.pow(sumX,0.5))*(Math.pow(sumY,0.5)));
        return corrCoefficient;
    }




    /** Most common  commands in types*/
    public void commonCommandsInSteps() {
        List<String> shList=new ArrayList<String>();
        List<String> batList=new ArrayList<String>();
        List<String> allCommandList=new ArrayList<String>();
        HashMap<String, String> commands = new HashMap<>();


        for (Pipeline pipeline : pipelines) {
            if (pipeline != null) {
                List<Stage> stageList=pipeline.getStages();
                if(stageList!=null){
                    for(Stage stage:stageList){
                        System.out.println("Stage name:"+stage.getName());
                        String stageName=stage.getName();
                        List<Branch> branchList=stage.getBranches();
                        if(branchList!=null){
                            for(Branch branch:branchList){
                                List<Step> stepList=branch.getSteps();
                                if(stepList!=null){
                                    for(Step step: stepList){
                                        String name=step.getName();  // name of step
                                        List<Argument> argsList = step.getArguments();
                                        if (argsList != null) {
                                            for (Argument argument : argsList) {
                                                Value argsValue = argument.getValue();
                                                String key = argument.getKey();  //key
                                                if (argsValue != null) {
                                                    String value = argsValue.getValue();//value
                                                    allCommandList.add(value);
                                                    commands.put(stageName,value);
                                                    if(name.equals("sh"))
                                                        shList.add(value);
                                                    else if(name.equals("bat"))
                                                        batList.add(value);

                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }

                    }
                }

            }
        }
        Collections.sort(shList);
        Collections.sort(batList);

        processList(shList,"LinuxCommands");
        processList(batList,"WindowsCommands");


    }
    /**Process a list of commands and classifies as per tasks*/
    public void processList(List<String> cmdlist,String fileType){
        String cmd;
        HashMap<String,Integer> commandTypes=new HashMap<>();
        int count=0;

        for(String command:cmdlist){
            cmd=command.split(" ")[0];
            if(commandTypes.containsKey(cmd)){
                count=commandTypes.get(cmd);
                commandTypes.put(cmd,count+1);
            }
            else
            {
                commandTypes.put(cmd,1);
            }
        }
        System.out.println("Cmd types "+fileType+": "+commandTypes.toString());
        convertToJsonFile(fileType,commandTypes);

    }

}
