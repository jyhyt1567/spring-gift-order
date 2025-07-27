package gift.component;

import gift.dto.KakaoAuthTokenResponseDto;
import gift.dto.OrderResponseDto;

public interface KakaoConnectClient {

    KakaoAuthTokenResponseDto retrieveToken(String code);

    String getEmail(String token);

    void sendMessage(OrderResponseDto responseDto, String token);
}
