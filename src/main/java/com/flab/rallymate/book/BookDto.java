package com.flab.rallymate.book;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "서적 DTO")
public record BookDto(
        @Schema(description = "ISBN 번호") Long isbn,
        @Schema(description = "제목") String title,
        @Schema(description = "가격") Integer price
) { }
