package com.example.tddfe.service;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.tddfe.model.Board;
import com.example.tddfe.util.PagerInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * BoardServiceImpl
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final  WebClient client;

	@Override
	public Integer selectBoardTotalCount() {
		
		return client
				.get()
				.uri("/totalCount")
				.retrieve()			
				.bodyToMono(Integer.class).block();
	}

	@Override
	public List<Board> boardList(PagerInfo pagerInfo) {
		
		log.info("========BoardServiceImpl=======boardList");
		int count = selectBoardTotalCount();
		log.info("========BoardServiceImpl=======count : {}", count);
		// int count = 10;
		int pageSize = 5;
		pagerInfo.init(pageSize, count);		
		String page = String.valueOf(pagerInfo.getPage());

		return client
			.get()
			// .uri("?size={size}&page={page}", pageSize, page)
			.uri(uriBuiler -> uriBuiler.path("")
				.queryParam("size", pageSize)
				.queryParam("page", page)
				.build())
			.retrieve()			
			.bodyToFlux(Board.class).collectList().block();
	
	}
	@Override
	public Board selectBoard(int num) {
		
		return client
				.get()
				.uri("/{num}", num)
				.retrieve()
				.bodyToMono(Board.class).block();
	}
	
	
	
}