package gift.service;

import gift.dto.KakaoAuthTokenResponseDto;
import gift.dto.OrderResponseDto;
import gift.entity.Member;

public interface KakaoService {

    KakaoAuthTokenResponseDto getKakaoToken(String code);

    KakaoAuthTokenResponseDto renewalKakaoToken(String refreshToken, Long memberId);

    void verifyKakaoAccountEmail(Member member);

    void sendMessage(OrderResponseDto responseDto, Member member);
}
