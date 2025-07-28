package gift.dto;

public record KakaoAuthTokenResponseDto(
        String token_type,
        String access_token,
        String refresh_token
) {

}
