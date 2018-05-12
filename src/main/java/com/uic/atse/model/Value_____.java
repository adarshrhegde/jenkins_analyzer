
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
    "arguments",
    "isLiteral",
    "value"
})
public class Value_____ {

    @JsonProperty("name")
    private String name;
    @JsonProperty("arguments")
    private List<Argument____> arguments = null;
    @JsonProperty("isLiteral")
    private Boolean isLiteral;
    @JsonProperty("value")
    private String value;
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

    @JsonProperty("arguments")
    public List<Argument____> getArguments() {
        return arguments;
    }

    @JsonProperty("arguments")
    public void setArguments(List<Argument____> arguments) {
        this.arguments = arguments;
    }

    @JsonProperty("isLiteral")
    public Boolean getIsLiteral() {
        return isLiteral;
    }

    @JsonProperty("isLiteral")
    public void setIsLiteral(Boolean isLiteral) {
        this.isLiteral = isLiteral;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
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
        return new ToStringBuilder(this).append("name", name).append("arguments", arguments).append("isLiteral", isLiteral).append("value", value).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value_____ that = (Value_____) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (arguments != null ? !arguments.equals(that.arguments) : that.arguments != null) return false;
        if (isLiteral != null ? !isLiteral.equals(that.isLiteral) : that.isLiteral != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return additionalProperties != null ? additionalProperties.equals(that.additionalProperties) : that.additionalProperties == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        result = 31 * result + (isLiteral != null ? isLiteral.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result;
    }
}
