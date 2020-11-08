package us.sep.biz.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogRequest {


    private String userName;

    private String operationName;

    private String message;

    private String ip;

    private String ifSuccess;

    private String time;

    private String createTime;



}
