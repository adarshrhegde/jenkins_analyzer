
package com.uic.atse.model;

import java.util.HashMap;
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
    "isLiteral",
    "value"
})
public class Value {

    @JsonProperty("isLiteral")
    private Boolean isLiteral;
    @JsonProperty("value")
    private String value;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
        return new ToStringBuilder(this).append("isLiteral", isLiteral).append("value", value).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value value1 = (Value) o;

        if (isLiteral != null ? !isLiteral.equals(value1.isLiteral) : value1.isLiteral != null) return false;
        if (value != null ? !value.equals(value1.value) : value1.value != null) return false;
        return additionalProperties != null ? additionalProperties.equals(value1.additionalProperties) : value1.additionalProperties == null;
    }

    @Override
    public int hashCode() {
        int result = isLiteral != null ? isLiteral.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result;
    }
}
