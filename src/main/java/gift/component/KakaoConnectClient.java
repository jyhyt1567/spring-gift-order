package gift.component;

import gift.dto.KakaoAuthTokenResponseDto;

public interface KakaoConnectClient {
    KakaoAuthTokenResponseDto retrieveToken(String code);

    String getEmail(String token);
}
