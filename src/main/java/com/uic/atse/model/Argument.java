
package com.uic.atse.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang.builder.ToStringBuilder;

/*@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "key",
    "isLiteral",
    "value"
})*/

//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Argument {

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("key")
    private String key;

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("value")
    private Value value;

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("isLiteral")
    private Boolean isLiteral;

    @JsonProperty("isLiteral")
    public Boolean getIsLiteral() {
        return isLiteral;
    }

    @JsonProperty("isLiteral")
    public void setIsLiteral(Boolean isLiteral) {
        this.isLiteral = isLiteral;
    }


    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("value")
    public Value getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Value value) {
        this.value = value;
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
        return new ToStringBuilder(this).append("key", key).append("value", value).append("isLiteral", isLiteral).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Argument argument = (Argument) o;

        if (key != null ? !key.equals(argument.key) : argument.key != null) return false;
        if (value != null ? !value.equals(argument.value) : argument.value != null) return false;
        if (isLiteral != null ? !isLiteral.equals(argument.isLiteral) : argument.isLiteral != null) return false;
        return additionalProperties != null ? additionalProperties.equals(argument.additionalProperties) : argument.additionalProperties == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (isLiteral != null ? isLiteral.hashCode() : 0);
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result;
    }
}
