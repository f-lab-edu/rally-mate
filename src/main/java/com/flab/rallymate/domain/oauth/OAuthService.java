package com.flab.rallymate.domain.oauth;

import com.flab.rallymate.common.response.BaseException;
import com.flab.rallymate.domain.member.MemberService;
import com.flab.rallymate.domain.member.dto.MemberDTO;
import com.flab.rallymate.domain.member.dto.MemberJoinReq;
import com.flab.rallymate.domain.member.dto.MemberLoginReq;
import com.flab.rallymate.domain.member.dto.MemberLoginRes;
import com.flab.rallymate.domain.oauth.constant.OAuthType;
import com.flab.rallymate.domain.oauth.dto.OAuthUserInfo;
import com.flab.rallymate.domain.oauth.social.SocialOAuth;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.flab.rallymate.common.response.ErrorCode.INVALID_SOCIAL_TYPE;

@Service
@Transactional(readOnly = true)
public class OAuthService {
    private final List<SocialOAuth> socialOauthList;
    private final HttpServletResponse response;
    private final MemberService memberService;

    public OAuthService(List<SocialOAuth> socialOauthList, HttpServletResponse response, MemberService memberService) {
        this.socialOauthList = socialOauthList;
        this.response = response;
        this.memberService = memberService;
    }

    public void requestConnect(OAuthType socialType) {
        SocialOAuth socialOAuth = this.findSocialOAuthByType(socialType);
        String redirectURL = socialOAuth.getOAuthRedirectUrl();
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public MemberLoginRes socialLogin(OAuthType socialType, String code) throws Exception {

        // 소셜 정보
        SocialOAuth socialOAuth = this.findSocialOAuthByType(socialType);

        OAuthUserInfo OAuthUserInfo = socialOAuth.getUserInfo(code);

        String socialId = OAuthUserInfo.id();
        String nickname = OAuthUserInfo.nickname();
        String email = OAuthUserInfo.email();

        Optional<MemberDTO> findMember = memberService.findMemberBy(email);
        if (findMember.isEmpty()) {
            MemberJoinReq joinReq = MemberJoinReq.of(nickname, email, socialId + socialOAuth.getInitPassword());
            memberService.join(joinReq);
        }

        MemberLoginReq loginReq = MemberLoginReq.of(email, socialId + socialOAuth.getInitPassword());

        return memberService.login(loginReq);
    }

    private SocialOAuth findSocialOAuthByType(OAuthType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new BaseException(INVALID_SOCIAL_TYPE));
    }

}
