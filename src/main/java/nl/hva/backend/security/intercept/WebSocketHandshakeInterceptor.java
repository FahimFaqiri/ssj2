package nl.hva.backend.security.intercept;

import jakarta.servlet.http.HttpServletRequest;
import nl.hva.backend.security.jwt.JwtUtils;
import nl.hva.backend.security.services.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandshakeInterceptor.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        final String url = request.getURI().toString();
        logger.info("Catching handshake from: {}", url);

        try {
            // verify url
            if (!url.contains("?")) {
                throw new Exception("Expected one query parameter, but received none.");
            }

            // verify query
            final String query = request.getURI().getQuery();
            if (query.contains("&")) {
                throw new Exception("Expected only one query parameter, but received multiple.");
            } else if (!query.contains("token")) {
                throw new Exception("Expected specific query parameter, but received other(s).");
            }

            // extract token
            final String token = "Bearer " + query.substring(6);
            logger.info("Received token: {}", token);

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        logger.info("Handshake successful!");

        // TODO: set user authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logger.info("Current user on websocket: {}", username);
    }
}
