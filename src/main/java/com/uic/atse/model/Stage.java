
package com.uic.atse.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "branches",
    "agent",
    "when",
    "post",
    "tools",
    "environment"
})
public class Stage {

    @JsonProperty("name")
    private String name;
    @JsonProperty("branches")
    private List<Branch> branches = null;
    @JsonProperty("agent")
    private Agent agent;
    @JsonProperty("when")
    private When when;
    @JsonProperty("post")
    private Post post;
    @JsonProperty("tools")
    private List<Tool> tools = null;
    @JsonProperty("environment")
    private List<Environment> environment = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("branches")
    public List<Branch> getBranches() {
        return branches;
    }

    @JsonProperty("branches")
    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    @JsonProperty("agent")
    public Agent getAgent() {
        return agent;
    }

    @JsonProperty("agent")
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    @JsonProperty("when")
    public When getWhen() {
        return when;
    }

    @JsonProperty("when")
    public void setWhen(When when) {
        this.when = when;
    }

    @JsonProperty("post")
    public Post getPost() {
        return post;
    }

    @JsonProperty("post")
    public void setPost(Post post) {
        this.post = post;
    }

    @JsonProperty("tools")
    public List<Tool> getTools() {
        return tools;
    }

    @JsonProperty("tools")
    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    @JsonProperty("environment")
    public List<Environment> getEnvironment() {
        return environment;
    }

    @JsonProperty("environment")
    public void setEnvironment(List<Environment> environment) {
        this.environment = environment;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("branches", branches).append("agent", agent).append("when", when).append("post", post).append("tools", tools).append("environment", environment).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stage stage = (Stage) o;

        if (name != null ? !name.equals(stage.name) : stage.name != null) return false;
        if (branches != null ? !branches.equals(stage.branches) : stage.branches != null) return false;
        if (agent != null ? !agent.equals(stage.agent) : stage.agent != null) return false;
        if (when != null ? !when.equals(stage.when) : stage.when != null) return false;
        if (post != null ? !post.equals(stage.post) : stage.post != null) return false;
        if (tools != null ? !tools.equals(stage.tools) : stage.tools != null) return false;
        if (environment != null ? !environment.equals(stage.environment) : stage.environment != null) return false;
        return additionalProperties != null ? additionalProperties.equals(stage.additionalProperties) : stage.additionalProperties == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (branches != null ? branches.hashCode() : 0);
        result = 31 * result + (agent != null ? agent.hashCode() : 0);
        result = 31 * result + (when != null ? when.hashCode() : 0);
        result = 31 * result + (post != null ? post.hashCode() : 0);
        result = 31 * result + (tools != null ? tools.hashCode() : 0);
        result = 31 * result + (environment != null ? environment.hashCode() : 0);
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result;
    }
}
