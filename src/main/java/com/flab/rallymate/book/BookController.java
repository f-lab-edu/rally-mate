package com.flab.rallymate.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "book", description = "서적 API") // name이 같은 것끼리 하나의 그룹으로 묶음
@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    @GetMapping("/test/{id}")
    @Operation(summary = "서적 테스트 API", description = "123을 반환하는 서적 테스트 API", responses = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = BookResponseDto.class)))
    })
    public String bootTest(@Parameter(name = "id", description = "book의 ISBN", in = ParameterIn.PATH)
                                            @PathVariable Long id) {
        return "123" + id.toString();
    }
}
