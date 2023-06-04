package com.practical.assignment.responseDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.practical.assignment.util.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectResponseDTO extends BaseResponseDTO {

    private String id;

    private String name;

    private Map<String, String> data;

    @JsonIgnore
    private String createdAt;

    @JsonIgnore
    private String updatedAt;

}
