package us.sep.message.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
  * @Author kana-cr
  * @Date  2020/10/10 9:15
  **/

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageBO {

    private String publisher;

    private String content;

    private String channel;

    private String examType;

    private String examDescription;
	
    private boolean ifPublish;
}
