package com.uic.atse.service;

import com.uic.atse.exception.PipelineAnalyzerException;
import com.uic.atse.model.Repository;

import java.util.List;

/**
 *  Used to query the remote repository and return results
 */
public interface RepositoryService {

    /**
     * Returns the list of repository objects satisfying the query results
     * @return
     */
    public List<Repository> getQueryResult() throws PipelineAnalyzerException;

}
