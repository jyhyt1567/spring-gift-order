package gift.service;

import gift.dto.KakaoAuthTokenResponseDto;

public interface KakaoLoginService {

    KakaoAuthTokenResponseDto getKakaoToken(String code);
}
