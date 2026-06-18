package com.furqan.ecommerce.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Comparator;

@Component
public class StartupSummaryPrinter {

    private final Environment environment;
    private final DataSource dataSource;
    private final RequestMappingHandlerMapping handlerMapping;

    public StartupSummaryPrinter(
            Environment environment,
            DataSource dataSource,
            @Qualifier("requestMappingHandlerMapping")
            RequestMappingHandlerMapping handlerMapping) {

        this.environment = environment;
        this.dataSource = dataSource;
        this.handlerMapping = handlerMapping;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void printStartupSummary(ApplicationReadyEvent event) {

        String appName = "E-Commerce Platform";

        String profile =
                environment.getActiveProfiles().length > 0
                        ? environment.getActiveProfiles()[0]
                        : "default";

        String port =
                environment.getProperty("server.port", "8080");

        String contextPath =
                environment.getProperty(
                        "server.servlet.context-path",
                        "/"
                );

        String javaVersion =
                System.getProperty("java.version");

        double startupTime =
                event.getTimeTaken().toMillis() / 1000.0;

        String databaseVersion = "Unknown";

        try (Connection connection = dataSource.getConnection()) {

            DatabaseMetaData metaData = connection.getMetaData();

            databaseVersion =
                    metaData.getDatabaseProductName()
                            + " "
                            + metaData.getDatabaseProductVersion();

        } catch (Exception ignored) {
        }
        System.out.println("=====================================================================================");
        System.out.println("E-Commerce Platform Started Successfully");
        System.out.println("=====================================================================================");

        System.out.printf(
                "Env=%s | Java=%s | Port=%s | DB=%s | Startup=%.2fs%n",
                profile,
                javaVersion,
                port,
                databaseVersion.split(" ")[0] + " " + databaseVersion.split(" ")[1],
                startupTime
        );
        System.out.println("=====================================================================================");

        System.out.printf(
                "Health   : http://localhost:%s/actuator/health%n",
                port
        );

        System.out.printf(
                "Mappings : http://localhost:%s/actuator/mappings%n",
                port
        );
        System.out.println("=====================================================================================");
        System.out.println("Registered APIs:");

        handlerMapping.getHandlerMethods()
                .entrySet()
                .stream()
                .sorted(
                        Comparator
                                .comparing(
                                        (java.util.Map.Entry<?, ?> entry) ->
                                                ((org.springframework.web.method.HandlerMethod)
                                                        entry.getValue())
                                                        .getBeanType()
                                                        .getName()
                                )
                                .thenComparing(
                                        entry ->
                                                ((org.springframework.web.servlet.mvc.method.RequestMappingInfo)
                                                        entry.getKey())
                                                        .getPatternValues()
                                                        .stream()
                                                        .findFirst()
                                                        .orElse("")
                                )
                )
                .forEach(entry -> {

                    var mapping = entry.getKey();
                    var handlerMethod = entry.getValue();

                    String httpMethod =
                            mapping.getMethodsCondition()
                                    .getMethods()
                                    .stream()
                                    .findFirst()
                                    .map(Enum::name)
                                    .orElse("ALL");

                    String controllerClass =
                            handlerMethod.getBeanType().getName();

                    mapping.getPatternValues()
                            .stream()
                            .filter(path -> !path.startsWith("/actuator"))
                            .filter(path -> !path.equals("/error"))
                            .map(path -> path.isBlank() ? "/" : path)
                            .distinct()
                            .forEach(path ->
                                    System.out.printf(
                                            "%-7s %-35s (%s)%n",
                                            httpMethod,
                                            path,
                                            controllerClass
                                    )
                            );
                });

        System.out.println("=================================================================================================================");
        System.out.println();
    }
}