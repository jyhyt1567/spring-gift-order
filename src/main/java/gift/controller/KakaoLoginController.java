package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.KakaoAuthTokenResponseDto;
import gift.entity.Member;
import gift.properties.Properties;
import gift.service.KakaoService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class KakaoLoginController {

    private final KakaoService kakaoService;

    private final Properties properties;

    private final String url;

    public KakaoLoginController(KakaoService kakaoService, Properties properties) {
        this.kakaoService = kakaoService;
        this.properties = properties;
        this.url = properties.getAuthUrl() +
                "/authorize?scope=talk_message&response_type=code&redirect_uri="+
                properties.getRedirectUri() +
                "&client_id=" +
                properties.getRestApiKey();
    }

    @GetMapping("kakao/login")
    public ResponseEntity<Void> kakaoLogin() {
        URI kakaoURL = URI.create(url);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(kakaoURL)
                .build();
    }

    @GetMapping
    public ResponseEntity<KakaoAuthTokenResponseDto> getKakaoToken(
            @RequestParam String code
    ) {
        return new ResponseEntity<>(kakaoService.getKakaoToken(code), HttpStatus.OK);
    }
}
