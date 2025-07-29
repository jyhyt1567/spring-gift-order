package gift.service;

import gift.dto.KakaoAuthTokenResponseDto;
import gift.dto.OrderResponseDto;
import gift.entity.KakaoAuth;
import gift.entity.Member;
import org.springframework.transaction.annotation.Transactional;

public interface KakaoService {

    KakaoAuthTokenResponseDto getKakaoToken(String code);

    KakaoAuthTokenResponseDto renewalKakaoToken(String refreshToken, Long memberId);

    void isValidateUser(Member member);

    void sendMessage(OrderResponseDto responseDto, Member member);
}
