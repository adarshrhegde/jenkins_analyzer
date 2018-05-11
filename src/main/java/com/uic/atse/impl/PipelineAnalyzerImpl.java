package com.uic.atse.impl;

/*
 Class for analyzing pipeline objects
*/

import com.uic.atse.model.*;

import java.util.HashMap;
import java.util.List;

public class PipelineAnalyzerImpl {

    // List of pipeline objects

    List<Pipeline> pipelines;

    public PipelineAnalyzerImpl(List<Pipeline> pipelines){
        this.pipelines=pipelines;
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
            System.out.println("getting post conditions for a pipeline ");
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
                    if (null != stage && null != stage.getBranches() ) {
                        for (Branch branch : stage.getBranches()){
                            if (null != branch && null != branch.getSteps() ) {
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
        System.out.println("Agent types and their frequencies across different pipelines "+step_freq.toString());
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
    }

    /**
     *  Perform analysis on pipeline objects
     */
    public void execute() {
//        frequentPostConditions();
//        frequentAgentTypes();
//        frequentStepTypes();
        frequentEnvVarTypes();


    }
}
