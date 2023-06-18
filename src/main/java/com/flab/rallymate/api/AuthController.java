package com.flab.rallymate.api;

import java.io.IOException;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flab.rallymate.auth.AuthService;
import com.flab.rallymate.auth.config.KakaoOAuthProperties;
import com.flab.rallymate.auth.dto.LoginResponseDTO;
import com.flab.rallymate.error.BaseException;
import com.flab.rallymate.error.BaseHttpResponse;
import com.flab.rallymate.error.ErrorCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final KakaoOAuthProperties kakaoOAuthProperties;

	@GetMapping("/kakao")
	@Operation(summary = "카카오 로그인 페이지 요청 API")
	public void test(HttpServletResponse response) {

		StringBuilder url = new StringBuilder("https://kauth.kakao.com/oauth/authorize?");
		url.append("client_id=").append(kakaoOAuthProperties.getClientId());
		url.append("&redirect_uri=").append(kakaoOAuthProperties.getCallbackUrl());
		url.append("&response_type=code");

		try {
			response.sendRedirect(url.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/login")
	@Operation(summary = "카카오 로그인 API", responses = {
		@ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class)))
	})
	public BaseHttpResponse<LoginResponseDTO> login(@RequestParam String code) {
		try {
			LoginResponseDTO loginResponseDTO = authService.kakaoLogin(code);
			return BaseHttpResponse.success(loginResponseDTO);
		} catch (JSONException e) {
			throw new BaseException(ErrorCode.NOT_FOUND_KAKAO_USER_INFO);
		}
	}
}





