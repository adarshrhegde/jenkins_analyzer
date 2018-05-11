package com.uic.atse.impl;

/*
 Class for analyzing pipeline objects

*/

import com.uic.atse.model.Pipeline;
import java.util.List;

public class PipelineAnalyzerImpl {

    // List of pipeline objects

    List<Pipeline> pipelines;

    public PipelineAnalyzerImpl(List<Pipeline> pipelines){
        this.pipelines=pipelines;
    }

}
