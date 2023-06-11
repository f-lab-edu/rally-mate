package com.flab.rallymate.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flab.rallymate.common.response.BaseHttpResponse;
import com.flab.rallymate.domain.member.MemberService;
import com.flab.rallymate.domain.member.dto.MemberLoginReq;
import com.flab.rallymate.domain.member.dto.MemberLoginRes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "member", description = "회원 API")
@RestController
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/login")
	@Operation(summary = "회원 로그인 API", responses = {
		@ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = MemberLoginRes.class))),
		@ApiResponse(responseCode = "400", description = "비밀번호 미일치", content = @Content(schema = @Schema(implementation = MemberLoginRes.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 회원", content = @Content(schema = @Schema(implementation = MemberLoginRes.class)))
	})
	public BaseHttpResponse<MemberLoginRes> login(@RequestBody MemberLoginReq loginReq) {
		return BaseHttpResponse.success(memberService.login(loginReq));
	}
}
