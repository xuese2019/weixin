package org.jiushan.weixin.send.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqMsgUtil implements Serializable {

    private String touser;

    private String template_id;

    //    回调地址
    private String url;

    private String topcolor;

    private List<Object> data = new ArrayList<>();
}
