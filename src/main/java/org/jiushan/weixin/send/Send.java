package org.jiushan.weixin.send;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

public class Send {
    private final String APPID = "wx1545c04654289f8a";
    private final String APPSECRET = "f0ba4bc5cc0e8d1c3a506e96bb8b48f4";
    private final String TOKENPATH = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" +
            APPID + "&secret=" + APPSECRET;

    /**
     * 发送消息
     *
     * @param body 消息内容
     * @return
     */
    public Mono<String> send(String body) {
        Mono<String> stringMono = openId();
        return stringMono
                .switchIfEmpty(Mono.just("空值"))
                .map(e -> {
                    System.out.print("返回值：" + e);
                    return e;
                });

    }

    /**
     * 获取openId
     *
     * @return
     */
    private Mono<String> openId() {
        return WebClient.create()
                .get()
                .uri(uriBuilder -> uriBuilder.path(TOKENPATH)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    private List<String> findUsers() {
//        //https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID
//        WebClient.create("https://api.weixin.qq.com/cgi-bin/user/get")
//                .get()
//                .uri(uriBuilder -> uriBuilder.path("")
//                        .queryParam("id", )
//                        .queryParam("name", name)
//                        .build())
        return null;
    }
}
