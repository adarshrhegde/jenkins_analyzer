package com.uic.atse.model;

import org.json.simple.JSONObject;

public class Repository {

    private String repositoryName;

    private String jenkinsFileUrl;

    private String jenkinsFileContent;

    private JSONObject json;

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public String getJenkinsFileContent() {
        return jenkinsFileContent;
    }

    public void setJenkinsFileContent(String jenkinsFileContent) {
        this.jenkinsFileContent = jenkinsFileContent;
    }

    public Repository() {
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getJenkinsFileUrl() {
        return jenkinsFileUrl;
    }

    public void setJenkinsFileUrl(String jenkinsFileUrl) {
        this.jenkinsFileUrl = jenkinsFileUrl;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "repositoryName='" + repositoryName + '\'' +
                ", jenkinsFileUrl='" + jenkinsFileUrl + '\'' +
                ", jenkinsFileContent='" + jenkinsFileContent + '\'' +
                ", json=" + json +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Repository that = (Repository) o;

        if (repositoryName != null ? !repositoryName.equals(that.repositoryName) : that.repositoryName != null)
            return false;
        if (jenkinsFileUrl != null ? !jenkinsFileUrl.equals(that.jenkinsFileUrl) : that.jenkinsFileUrl != null)
            return false;
        if (jenkinsFileContent != null ? !jenkinsFileContent.equals(that.jenkinsFileContent) : that.jenkinsFileContent != null)
            return false;
        return json != null ? json.equals(that.json) : that.json == null;
    }

    @Override
    public int hashCode() {
        int result = repositoryName != null ? repositoryName.hashCode() : 0;
        result = 31 * result + (jenkinsFileUrl != null ? jenkinsFileUrl.hashCode() : 0);
        result = 31 * result + (jenkinsFileContent != null ? jenkinsFileContent.hashCode() : 0);
        result = 31 * result + (json != null ? json.hashCode() : 0);
        return result;
    }
}
