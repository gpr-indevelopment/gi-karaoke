package com.gprindevelopment.gikaraoke;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public PushNotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void pushFrontendUpdate() {
        simpMessagingTemplate.convertAndSend("/topic/update", "Update!");
    }
}
