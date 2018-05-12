
package com.uic.atse.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "pipeline"
})
public class Json {

    @JsonProperty("pipeline")
    private Pipeline pipeline;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("pipeline")
    public Pipeline getPipeline() {
        return pipeline;
    }

    @JsonProperty("pipeline")
    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Json json = (Json) o;

        if (pipeline != null ? !pipeline.equals(json.pipeline) : json.pipeline != null) return false;
        return additionalProperties != null ? additionalProperties.equals(json.additionalProperties) : json.additionalProperties == null;
    }

    @Override
    public int hashCode() {
        int result = pipeline != null ? pipeline.hashCode() : 0;
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result;
    }
}
