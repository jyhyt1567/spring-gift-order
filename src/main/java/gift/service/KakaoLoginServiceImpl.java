package gift.service;

import gift.component.KakaoConnectClient;
import gift.dto.KakaoAuthTokenResponseDto;
import gift.entity.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginServiceImpl implements KakaoLoginService {

    private final KakaoConnectClient connectClient;

    private final MemberService memberService;

    KakaoLoginServiceImpl(KakaoConnectClient connectClient, MemberService memberService){
        this.connectClient = connectClient;
        this.memberService = memberService;
    }

    @Override
    public KakaoAuthTokenResponseDto getKakaoToken(String code) {
        return connectClient.retrieveToken(code);
    }

    @Override
    public Member isValidateUser(String token) {
        String email = connectClient.getEmail(token);
        return memberService.findMemberByEmailOrElseThrow(email);
    }
}
