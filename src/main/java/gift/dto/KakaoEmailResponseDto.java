package gift.dto;

public record KakaoEmailResponseDto(
        KakaoAccount kakao_account
) {
    public String getEmail() {
        return kakao_account.email();
    }
}
