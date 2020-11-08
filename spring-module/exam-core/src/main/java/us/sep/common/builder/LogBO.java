package us.sep.common.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogBO {

    private String ip;

    private String name;

    private String action;

    private String time;

    private String message;
}
