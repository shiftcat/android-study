package com.example.android;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonPropertyOrder({"data1", "data2", "data3"})
@Data
public class ResponseVO
{

    @JsonProperty("data1")
    private String data1;

    @JsonProperty("data2")
    private Integer data2;

    @JsonProperty("data3")
    private Double data3;

}
