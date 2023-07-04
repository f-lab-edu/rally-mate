package com.flab.rallymate.api;

import com.flab.rallymate.auth.AuthService;
import com.flab.rallymate.auth.dto.LoginResponseDTO;
import com.flab.rallymate.config.oauth.KakaoOAuthProperties;
import com.flab.rallymate.error.BaseHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final KakaoOAuthProperties kakaoOAuthProperties;

	@GetMapping("/kakao")
	@Operation(summary = "카카오 로그인 페이지 요청 API")
	public void requestKakaoLoginPage(HttpServletResponse response) {

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
	@Operation(summary = "카카오 로그인 API")
	public BaseHttpResponse<LoginResponseDTO> login(@RequestParam String code) {
		var loginResponseDTO = authService.kakaoLogin(code);
		return BaseHttpResponse.success(loginResponseDTO);
	}

	@PostMapping("/re-issue")
	public BaseHttpResponse<LoginResponseDTO> reIssue(@RequestHeader("RefreshToken") String refreshToken) {
		var loginResponseDTO = authService.reIssue(refreshToken);
		return BaseHttpResponse.success(loginResponseDTO);
	}
}





