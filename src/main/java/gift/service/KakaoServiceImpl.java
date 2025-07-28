package gift.service;

import gift.component.KakaoConnectClient;
import gift.dto.KakaoAuthTokenResponseDto;
import gift.dto.OrderResponseDto;
import gift.entity.KakaoAuth;
import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.KakaoAuthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KakaoServiceImpl implements KakaoService {

    private final KakaoConnectClient connectClient;

    private final KakaoAuthRepository kakaoAuthRepository;
    private final MemberService memberService;

    KakaoServiceImpl(KakaoConnectClient connectClient, KakaoAuthRepository kakaoAuthRepository,
            MemberService memberService) {
        this.connectClient = connectClient;
        this.kakaoAuthRepository = kakaoAuthRepository;
        this.memberService = memberService;
    }

    @Override
    @Transactional
    public KakaoAuthTokenResponseDto getKakaoToken(String code) {
        KakaoAuthTokenResponseDto responseDto = connectClient.retrieveToken(code);
        String email = connectClient.getEmail(responseDto.access_token());
        Member member = memberService.findMemberByEmailOrElseThrow(email);
        KakaoAuth kakaoAuth = new KakaoAuth(member.getId(), responseDto.access_token(),
                responseDto.refresh_token());
        kakaoAuthRepository.save(kakaoAuth);
        return responseDto;
    }

    @Override
    @Transactional
    public KakaoAuthTokenResponseDto renewalKakaoToken(String refreshToken, Long memberId) {
        KakaoAuth kakaoAuth = findKakaoAuthByIdOrElseThrow(memberId);
        KakaoAuthTokenResponseDto responseDto = connectClient.renewalToken(refreshToken);
        kakaoAuth.renewalAccessToken(responseDto.access_token());
        if (responseDto.refresh_token() != null) {
            kakaoAuth.renewalRefreshToken(responseDto.refresh_token());
        }
        return responseDto;
    }

    @Override
    public void isValidateUser(Member member) {
        String email;
        String memberEmail = member.getEmail();
        KakaoAuth kakaoAuth = findKakaoAuthByIdOrElseThrow(member.getId());
        try {
            email = connectClient.getEmail(kakaoAuth.getAccessToken());
        } catch (CustomException e) {
            renewalKakaoToken(kakaoAuth.getRefreshToken(), member.getId());
            email = connectClient.getEmail(kakaoAuth.getAccessToken());
        }
        if (!memberEmail.equals(email)) {
            throw new CustomException(ErrorCode.LoginAnotherAccount);
        }
    }

    @Override
    public void sendMessage(OrderResponseDto responseDto, Member member) {
        isValidateUser(member);
        KakaoAuth kakaoAuth = findKakaoAuthByIdOrElseThrow(member.getId());
        connectClient.sendMessage(responseDto, kakaoAuth.getAccessToken());
    }

    private KakaoAuth findKakaoAuthByIdOrElseThrow(Long memberId) {
        return kakaoAuthRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotKakaoLogined));
    }
}
