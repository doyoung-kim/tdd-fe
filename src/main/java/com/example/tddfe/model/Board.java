package com.example.tddfe.model;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Board
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

     
	private Integer num;
	private String title;
	private String contents;
	private String writeName;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	// @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.S][.SS][.SSS]")
	private LocalDateTime writeDate;	 
	private String modifyName;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	// @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.S][.SS][.SSS]")
	private LocalDateTime  modifyDate;
	private Integer cnt;
	private String writeId;
	private String modifyId;
    
}