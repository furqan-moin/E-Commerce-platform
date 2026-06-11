/*
package com.furqan.ecommerce.configs;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class EndpointPrinter {

    private final RequestMappingHandlerMapping handlerMapping;

    public EndpointPrinter(
            @Qualifier("requestMappingHandlerMapping")
            RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {

        System.out.println("\nRegistered APIs:\n");

        handlerMapping.getHandlerMethods()
                .forEach((mapping, handlerMethod) -> {

                    String controllerClass =
                            handlerMethod.getBeanType().getName();

                    String httpMethod =
                            mapping.getMethodsCondition()
                                    .getMethods()
                                    .stream()
                                    .findFirst()
                                    .map(Enum::name)
                                    .orElse("ALL");

                    mapping.getPatternValues()
                            .stream()
                            .filter(path -> !path.startsWith("/actuator"))
                            .filter(path -> !path.equals("/error"))
                            .forEach(path ->
                                    System.out.printf(
                                            "    %-7s %-50s (%s)%n",
                                            httpMethod,
                                            path,
                                            controllerClass
                                    )
                            );
                });
    }
}*/
