package com.flab.rallymate.api;

import com.flab.rallymate.common.response.BaseHttpResponse;
import com.flab.rallymate.domain.member.dto.MemberLoginRes;
import com.flab.rallymate.domain.oauth.OAuthService;
import com.flab.rallymate.domain.oauth.constant.OAuthType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "oauth", description = "소셜 로그인 API")
@CrossOrigin
@RestController
@RequestMapping(value = "/auth")
public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping(value = "/{socialName}")
    @Operation(summary = "소셜 로그인 API 요청")
    public void socialLoginType(@PathVariable(name = "socialName") OAuthType socialLoginType) {
        oAuthService.requestConnect(socialLoginType);
    }

    @GetMapping(value = "/{socialName}/callback")
    @Operation(summary = "소셜 로그인 API Callback 처리")
    public BaseHttpResponse<MemberLoginRes> callback(@PathVariable(name = "socialName") OAuthType socialLoginType,
                                                     @RequestParam(name = "code") String code) throws Exception {

        return BaseHttpResponse.success(oAuthService.socialLogin(socialLoginType, code));
    }

}
