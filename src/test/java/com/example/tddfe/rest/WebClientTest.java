package com.example.tddfe.rest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.tddfe.model.Board;

import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@Slf4j
public class WebClientTest {

    private MockWebServer server;
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector();
        server = new MockWebServer();
        webClient = WebClient
                .builder()
                .clientConnector(connector)
                .baseUrl(server.url("/api/boards").toString())
                .build();

    }

    @AfterEach
    void shutdown() throws IOException {
        server.shutdown();
    }

    @Test
    void testBoardList() throws Exception {
        // Path path = Paths.get("src/test/resources/list.json");
        // String jsonStr = Files.readAllLines(path).get(0);

        String jsonStr = FileUtils.readFileToString(new ClassPathResource("list.json").getFile(), StandardCharsets.UTF_8);
     
        log.info("=========jsonStr : {}", jsonStr);

        MockResponse mockResponse = new MockResponse()

                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody(jsonStr);

        server.enqueue(mockResponse);

        int size = 10;
        int page = 1;
        List<Board> boardList = webClient
                .get()
                .uri(uriBuiler -> uriBuiler.path("")
                        .queryParam("size", size)
                        .queryParam("page", page)
                        .build())

                .retrieve()
                .bodyToFlux(Board.class).collectList().block();

        // when

        // then
        // assertThat(boardList.size()).isGreaterThan(1);
        log.info("=========boardList : {}", boardList);

    }

}
