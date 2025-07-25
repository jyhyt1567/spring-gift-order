package gift.service;

import gift.dto.KakaoAuthTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginServiceImpl implements KakaoLoginService {

    private final RestClient client = RestClient.builder().build();
    private final String requestUrl = "https://kauth.kakao.com/oauth/token";

    @Value("${REST_API_KEY}")
    private String REST_API_KEY;

    @Override
    public KakaoAuthTokenResponseDto getKakaoToken(String code) {

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", REST_API_KEY);
        body.add("redirect_uri", "http://localhost:8080");
        body.add("code", code);

        ResponseEntity<KakaoAuthTokenResponseDto> response = client.post()
                .uri(requestUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(body)
                .retrieve()
                .toEntity(KakaoAuthTokenResponseDto.class);
        KakaoAuthTokenResponseDto result = response.getBody();
        return result;
    }
}
