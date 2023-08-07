package com.flab.rallymate.api;

import com.flab.rallymate.domain.post.PostService;
import com.flab.rallymate.domain.post.dto.PostRequestDTO;
import com.flab.rallymate.error.BaseHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "메이트 구인 글 등록 API")
    public BaseHttpResponse<String> addPost(@Valid @RequestBody PostRequestDTO postRequestDTO) {
        postService.addPost(postRequestDTO);
        return BaseHttpResponse.successWithNoContent("구인글을 등록했어요.");
    }
}