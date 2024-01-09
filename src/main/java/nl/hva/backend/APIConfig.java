package nl.hva.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class APIConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(APIConfig.class);

    private void exposeDirectory(String dir, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dir);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        if (dir.startsWith("../")) {
            dir = dir.replace("../", "");
        }

        registry.addResourceHandler(String.format("/%s/**", dir))
                .addResourceLocations(String.format("file:/%s/", uploadPath));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("Adding cors mappings");
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("users/profile/photo", registry);
    }
}
