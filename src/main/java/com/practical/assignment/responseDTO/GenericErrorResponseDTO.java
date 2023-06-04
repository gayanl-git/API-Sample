package com.practical.assignment.responseDTO;

import com.practical.assignment.util.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class GenericErrorResponseDTO extends BaseResponseDTO {

    private Date timestamp;
    private int status;
    private String error;
    private String path;

}
