package gift.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import gift.component.KakaoConnectClient;
import gift.dto.KakaoAuthTokenResponseDto;
import gift.entity.KakaoAuth;
import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.KakaoAuthRepository;
import gift.service.KakaoServiceImpl;
import gift.service.MemberService;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KakaoServiceTest {
    @Mock
    KakaoConnectClient connectClient;

    @Mock
    KakaoAuthRepository kakaoAuthRepository;

    @Mock
    MemberService memberService;

    @InjectMocks
    KakaoServiceImpl kakaoService;

    private Long memberId = 1L;
    private String password = "asd";
    private String email = "asd@asd.asd";
    private String code = "JustAuthCode";
    private String accessToken = "access token";
    private String refreshToken = "refresh token";
    private Member member = new Member(memberId, email, password, "user");

    private KakaoAuth kakaoAuth = new KakaoAuth(memberId, accessToken, refreshToken);

    @Test
    @DisplayName("카카오 토큰 발급 성공 테스트")
    void 카카오_토큰_발급_성공() {
        KakaoAuthTokenResponseDto responseDto =
                new KakaoAuthTokenResponseDto("bearer", accessToken, refreshToken);
        given(connectClient.retrieveToken(code)).willReturn(responseDto);
        given(connectClient.getEmail(accessToken)).willReturn(email);
        given(memberService.findMemberByEmailOrElseThrow(email)).willReturn(member);
        given(kakaoAuthRepository.save(any())).willReturn(kakaoAuth);

        KakaoAuthTokenResponseDto actual = kakaoService.getKakaoToken(code);

        assertAll(
                () -> AssertionsForClassTypes.assertThat(actual.token_type()).isEqualTo("bearer"),
                () -> AssertionsForClassTypes.assertThat(actual.access_token()).isEqualTo(accessToken),
                () -> AssertionsForClassTypes.assertThat(actual.refresh_token()).isEqualTo(refreshToken)
        );
    }

    @Test
    @DisplayName("카카오 토큰 갱신 성공 테스트")
    void 카카오_토큰_갱신_성공() {
        given(kakaoAuthRepository.findById(memberId)).willReturn(Optional.of(kakaoAuth));

        KakaoAuthTokenResponseDto responseDto = new KakaoAuthTokenResponseDto("bearer", "new access token", "new refresh token");
        given(connectClient.renewalToken(refreshToken)).willReturn(responseDto);

        KakaoAuthTokenResponseDto actual = kakaoService.renewalKakaoToken(refreshToken, memberId);

        assertAll(
                () -> AssertionsForClassTypes.assertThat(actual.token_type()).isEqualTo("bearer"),
                () -> AssertionsForClassTypes.assertThat(actual.access_token()).isEqualTo(responseDto.access_token()),
                () -> AssertionsForClassTypes.assertThat(actual.refresh_token()).isEqualTo(responseDto.refresh_token())
        );
    }

    @Test
    @DisplayName("카카오 토큰 검증 성공 테스트")
    void 카카오_토큰_검증_성공() {
        given(kakaoAuthRepository.findById(memberId)).willReturn(Optional.of(kakaoAuth));
        given(connectClient.getEmail(accessToken)).willReturn(email);

        kakaoService.isValidateUser(member);
    }

    @Test
    @DisplayName("엑세스 토큰 만료 시 갱신하고 성공 테스트")
    void 카카오_토큰_검증_갱신_성공() {
        given(kakaoAuthRepository.findById(memberId)).willReturn(Optional.of(kakaoAuth));
        given(connectClient.getEmail(accessToken)).willThrow(new CustomException(ErrorCode.KakaoAuthClientError));
        KakaoAuthTokenResponseDto responseDto =
                new KakaoAuthTokenResponseDto("bearer", "new access token", "new refresh token");
        given(connectClient.renewalToken(refreshToken)).willReturn(responseDto);
        given(connectClient.getEmail(responseDto.access_token())).willReturn(email);
        kakaoService.isValidateUser(member);
        assertAll(
                () -> AssertionsForClassTypes.assertThat(kakaoAuth.getAccessToken()).isEqualTo(responseDto.access_token()),
                () -> AssertionsForClassTypes.assertThat(kakaoAuth.getRefreshToken()).isEqualTo(responseDto.refresh_token())
        );
    }

    @Test
    @DisplayName("카카오 다른 계정으로 로그인 시 검증 실패 테스트")
    void 카카오_다른이메일_토큰_검증_실패() {
        given(kakaoAuthRepository.findById(memberId)).willReturn(Optional.of(kakaoAuth));
        given(connectClient.getEmail(accessToken)).willReturn("other@asd.asd");

        CustomException e = Assertions.assertThrows(CustomException.class,
                () -> kakaoService.isValidateUser(member));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.LoginAnotherAccount);
    }
}
