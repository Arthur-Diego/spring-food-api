package com.spring.food.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class ErrorCustomized {

    private Integer status;
    private String type;
    private String title;
    private String detail;

}
