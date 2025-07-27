package gift.service;

import gift.component.KakaoConnectClient;
import gift.dto.KakaoAuthTokenResponseDto;
import gift.dto.OrderResponseDto;
import gift.entity.Member;
import org.springframework.stereotype.Service;

@Service
public class KakaoServiceImpl implements KakaoService {

    private final KakaoConnectClient connectClient;

    private final MemberService memberService;

    KakaoServiceImpl(KakaoConnectClient connectClient, MemberService memberService) {
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

    @Override
    public void sendMessage(OrderResponseDto responseDto, String token) {
        connectClient.sendMessage(responseDto, token);
    }
}
