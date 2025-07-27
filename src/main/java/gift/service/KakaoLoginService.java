package gift.service;

import gift.dto.KakaoAuthTokenResponseDto;
import gift.entity.Member;

public interface KakaoLoginService {

    KakaoAuthTokenResponseDto getKakaoToken(String code);

    Member isValidateUser(String token);
}
