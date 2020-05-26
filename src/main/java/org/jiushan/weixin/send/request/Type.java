package org.jiushan.weixin.send.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Type implements Serializable {

    private String value;

    private String color;
}
