package nl.hva.backend;

import nl.hva.backend.security.intercept.WebSocketHandshakeInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        logger.info("Configuring message broker");

        config.setApplicationDestinationPrefixes("/chat");
        config.enableSimpleBroker("/queue");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        logger.info("Configuring WebSocket transport");

        // set heartbeat settings
        registration.setSendTimeLimit(20 * 1000);
        registration.setSendBufferSizeLimit(8 * 1024);
        registration.setMessageSizeLimit(8 * 1024);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        logger.info("Registering Stomp endpoints");

        registry.addEndpoint("/connect")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new WebSocketHandshakeInterceptor())
                .withSockJS();
    }
}
