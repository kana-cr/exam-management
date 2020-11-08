package us.sep.biz.message.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    @NotBlank
    private String publisher;

    @NotBlank
    private String content;

    @NotBlank
    private String channel;

    @NotBlank
    private String examType;

    @NotBlank
    private String examDescription;
}
