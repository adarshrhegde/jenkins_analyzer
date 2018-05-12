
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
    "result",
    "json"
})
public class Data {

    @JsonProperty("result")
    private String result;
    @JsonProperty("json")
    private Json json;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("result")
    public String getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(String result) {
        this.result = result;
    }

    @JsonProperty("json")
    public Json getJson() {
        return json;
    }

    @JsonProperty("json")
    public void setJson(Json json) {
        this.json = json;
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

        Data data = (Data) o;

        if (result != null ? !result.equals(data.result) : data.result != null) return false;
        if (json != null ? !json.equals(data.json) : data.json != null) return false;
        return additionalProperties != null ? additionalProperties.equals(data.additionalProperties) : data.additionalProperties == null;
    }

    @Override
    public int hashCode() {
        int result1 = result != null ? result.hashCode() : 0;
        result1 = 31 * result1 + (json != null ? json.hashCode() : 0);
        result1 = 31 * result1 + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result1;
    }
}
