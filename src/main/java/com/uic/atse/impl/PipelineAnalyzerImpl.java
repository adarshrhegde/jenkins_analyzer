package com.uic.atse.impl;

/*
 Class for analyzing pipeline objects
*/

import com.uic.atse.model.Condition;
import com.uic.atse.model.Condition_;
import com.uic.atse.model.Pipeline;
import com.uic.atse.model.Stage;

import java.util.HashMap;
import java.util.List;

public class PipelineAnalyzerImpl {

    // List of pipeline objects

    List<Pipeline> pipelines;

    public PipelineAnalyzerImpl(List<Pipeline> pipelines){
        this.pipelines=pipelines;
    }

    // What are the most frequent post-condition blocks in the post section within jenkins pipelines?
    public void FrequentPostConditionBlocks() {
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
                    System.out.println("getting stages ");
                    if(null !=stage && null != stage.getPost() && null!=stage.getPost().getConditions()) {
                        System.out.println("getting post conditions for a stage ");
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

}
