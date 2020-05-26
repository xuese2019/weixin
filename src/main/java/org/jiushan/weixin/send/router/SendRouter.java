package org.jiushan.weixin.send.router;

import org.jiushan.weixin.send.handler.SendHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SendRouter {

    @Bean
    public RouterFunction<ServerResponse> sendR(SendHandler sendHandler) {
        return RouterFunctions.nest(
                RequestPredicates.path("/send"),
                RouterFunctions.route(
                        RequestPredicates.GET("/send"),
                        sendHandler::send
                )
        );
    }
}
