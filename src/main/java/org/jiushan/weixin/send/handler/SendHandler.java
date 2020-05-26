package org.jiushan.weixin.send.handler;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.jiushan.weixin.send.request.ReqMsgUtil;
import org.jiushan.weixin.send.request.User;
import org.jiushan.weixin.send.resp.RespUserUtil;
import org.jiushan.weixin.send.resp.RespUtil;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
public class SendHandler {

    private final static String APPID = "wx1545c04654289f8a";
    private final static String APPSECRET = "f0ba4bc5cc0e8d1c3a506e96bb8b48f4";
    //    获取token
    private final static String TOKENPATH = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" +
            APPID + "&secret=" + APPSECRET;
    //    获取所有用户
    private final static String USERPATH = "https://api.weixin.qq.com/cgi-bin/user/get";
    //    模板ID
    private final static String MODELID = "cuGdK3P0oo9JCD6LisHMH_AD8bkPG-L_AWTSmWo4z6w";
    //    发送消息地址
    private final static String MESGPATH = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    /**
     * 发送消息
     *
     * @return
     */
    public Mono<ServerResponse> send(ServerRequest request) {
//        普通java版
//        try {
//            String get = HttpUtil.doPost("GET", TOKENPATH, "multipart/form-data", "UTF-8", 3000, 3000);
//            System.out.println(get);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        java 函数版
//        获取token
        openId()
                .subscribe(e -> {
//            获取关注的人
                    findUser(e.getAccess_token())
                            .subscribe(u -> {
//                                给每个人发送消息
                                String[] openid = u.getData().getOpenid();
                                for (int i = 0; i < openid.length; i++) {
                                    sendMsg(openid[i], e.getAccess_token())
                                            .subscribe(System.out::println);
                                }
                            });
                });
        return Mono.empty();
    }

    /**
     * 根据模板和用户发送消息
     *
     * @param open_id
     * @param access_token
     * @return
     */
    private Mono<String> sendMsg(String open_id, String access_token) {
        ReqMsgUtil reqMsgUtil = new ReqMsgUtil();
        reqMsgUtil.setTouser(open_id);
        reqMsgUtil.setTemplate_id(MODELID);
        reqMsgUtil.setUrl(null);
        reqMsgUtil.setTopcolor("#FF0000");
        User user = new User();
        user.setColor("#ccc");
        user.setValue("测试");
        reqMsgUtil.getData().add(user);
//        https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
        return webClient()
                .post()
                .uri(MESGPATH + access_token)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(reqMsgUtil), ReqMsgUtil.class)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(5000));
    }


    /**
     * 获取所有用户
     *
     * @return
     */
    private Mono<RespUserUtil> findUser(String access_token) {
//        https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID
        return webClient()
                .get()
                .uri(USERPATH + "?access_token=" + access_token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(RespUserUtil.class)
                .timeout(Duration.ofMillis(5000));
    }

    /**
     * 获取openid
     *
     * @return
     */
    private Mono<RespUtil> openId() {
        return webClient()
                .get()
                .uri(TOKENPATH)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(RespUtil.class)
                .timeout(Duration.ofMillis(5000));
    }

    /**
     * webClient对象
     *
     * @return
     */
    private WebClient webClient() {
        //        设置ssl
        reactor.netty.http.client.HttpClient secure = HttpClient.create()
                .secure(t -> t.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(secure))
                .build();
    }

}
