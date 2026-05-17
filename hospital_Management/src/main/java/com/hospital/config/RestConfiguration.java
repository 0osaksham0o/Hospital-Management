package com.hospital.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        // ── Expose @Id fields for every JPA entity ──────────────────────────
        Class<?>[] entityClasses = entityManager.getMetamodel().getEntities()
                .stream()
                .map(Type::getJavaType)
                .toArray(Class<?>[]::new);
        config.exposeIdsFor(entityClasses);

        // ── Disable write operations on repository-backed endpoints ─────────
        config.getExposureConfiguration()
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(
                        HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH))
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(
                        HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH));

        // ── CORS for all origins ─────────────────────────────────────────────
        cors.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowCredentials(false)
                .maxAge(3600);
    }
}

