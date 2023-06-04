package com.practical.assignment.responseDTO;

import com.practical.assignment.util.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDTO extends BaseResponseDTO {

    private String error;

}
