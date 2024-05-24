package com.green.webclienttest.picsum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.webclienttest.picsum.model.PicSumGetReq;
import com.green.webclienttest.picsum.model.PicSumGetRes;
import io.netty.channel.ChannelOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import javax.swing.tree.RowMapper;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class PicSumService {
    private final WebClient webClient;
    public PicSumService(){
        TcpClient tcpClient=TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000);
                //1000분의 1초 5000=5초 기다리겠다)
        ExchangeStrategies es= ExchangeStrategies.builder()
                .codecs(config->config.defaultCodecs().maxInMemorySize(-1)/*메모리 상관 없이 쓰겠다 양수면 그만큼만 사용가능*/)
                /*//익명클래스로 객체화
                    .codecs(new Consumer<ClientCodecConfigurer>() {
                    @Override
                    public void accept(ClientCodecConfigurer config) {
                        config.defaultCodecs().maxInMemorySize(-1);
                    }
                })*/
                .build();
        this.webClient= WebClient.builder()
                .exchangeStrategies(es)
                .baseUrl("https://picsum.photos")//공통 주소 이게 있음으로 인해 뒤쪽만 요청 보내도 됨
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public List<PicSumGetRes> getPicSum(PicSumGetReq p){
        String json=webClient.get().uri(uriBuilder -> uriBuilder.path("/v2/list")//나머지 주소값
                                                    .queryParam("page", p.getPage())
                                                    .queryParam("limit", p.getLimit())
                                                     .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper om=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);//매칭되는 애가 없어도 에러 터트리지마라
        List<PicSumGetRes> picSumList =null;
        try {
            JsonNode jsonNode=om.readTree(json); //Node형식 데이터 형식에서 보면 객체 하나가 node형식 //오류를 던지고 있다
            picSumList = om.convertValue(jsonNode.at(""), new TypeReference<List<PicSumGetRes>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return picSumList;
    }
}
