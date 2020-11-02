package us.sep.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageReceiver {
    public void receiveMessage(String message) {
        log.info(message);
    }
}
