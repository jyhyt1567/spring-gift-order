package gift.service;

import gift.dto.KakaoAuthTokenResponseDto;
import gift.dto.OrderResponseDto;
import gift.entity.Member;

public interface KakaoService {

    KakaoAuthTokenResponseDto getKakaoToken(String code);

    Member isValidateUser(String token);

    void sendMessage(OrderResponseDto responseDto, String token);
}
