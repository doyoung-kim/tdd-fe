package com.example.tddfe.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.tddfe.model.Board;
import com.example.tddfe.util.PagerInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@Slf4j
public class BoardServiceImplTest {
    private static MockWebServer mockWebServer;

    private ObjectMapper objectMapper;
    private BoardServiceImpl service;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector();
        // final String baseUrl = String.format("http://localhost:%s",
        // mockWebServer.getPort());
        WebClient webClient = WebClient
                .builder()
                .clientConnector(connector)
                .baseUrl(mockWebServer.url("/api/boards").toString())
                .build();
        service = new BoardServiceImpl(webClient);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void testBoardList() throws JsonProcessingException {
        // given
        int page = 1;
        int size = 3;
        int totalCount = 10;

        PagerInfo pagerInfo = new PagerInfo(page, size, totalCount);
        // MockResponse mockResponse = new MockResponse()
        // .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        // .setBody(objectMapper.writeValueAsString(getBoardList()))
        // .setResponseCode(HttpStatus.OK.value());

        String responseBody = objectMapper.writeValueAsString(getBoardList());
        String responseBody2 = totalCount + "";
        mockWebServer.setDispatcher(getDispatcher(responseBody, responseBody2));
        // mockWebServer.enqueue(mockResponse);
        List<Board> boardList = service.boardList(pagerInfo);
        assertThat(boardList.size()).isGreaterThan(1);

        log.info("=====boardList : {}", boardList);

    }

    @Test
    void testSelectBoard() throws JsonProcessingException{
        int num =5;
        String responseBody = getBoard(num);
        mockWebServer.setDispatcher(getDispatcher(responseBody, null));
        Board board = service.selectBoard(num);
        log.info("=====testSelectBoard board : {}", board);
        assertThat(board.getNum()).isEqualTo(num);
    }

    private List<Board> getBoardList() {
        List<Board> boardList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Board board = Board.builder()
                    .num(i + 1)
                    .title("test-" + i)
                    .contents("testcontents-" + i)
                    .writeId("user1")
                    .writeName("테스터")

                    .build();
            boardList.add(board);
        }

        return boardList;
    }

    private String getBoard(int num) throws JsonProcessingException{
      return  objectMapper.writeValueAsString( Board.builder()
            .num(num)
            .title("test-"+num)
            .contents("testcontents-"+num)
            .writeId("user1")
            .writeName("테스터")
            .build());

    }

    private Dispatcher getDispatcher(String body, String body2) {
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                // var method = request.getMethod();
                var path = request.getPath().split("\\?")[0];
                log.info("======path: {}", path);
                MockResponse mockResponse = new MockResponse()
                        .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .setResponseCode(HttpStatus.OK.value());
                switch (path) {
                    case "/api/boards/totalCount":
                        return mockResponse.setBody(body2);
                    default:
                        return mockResponse.setBody(body);

                }
                
            }
        };
        return dispatcher;
    }
}
