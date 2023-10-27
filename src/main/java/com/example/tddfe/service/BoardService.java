package com.example.tddfe.service;
import java.io.IOException;
import java.util.List;

import com.example.tddfe.model.AppUser;
import com.example.tddfe.model.Board;
import com.example.tddfe.model.ResultMsg;
import com.example.tddfe.util.PagerInfo;




/**
 * BoardService
 */
public interface BoardService {
    public Integer selectBoardTotalCount() throws Exception;
	public List<Board> boardList(PagerInfo pagerInfo) throws Exception;		
	public Board selectBoard(int num) throws Exception; 	
	
    
}