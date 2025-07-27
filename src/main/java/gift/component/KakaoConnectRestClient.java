package gift.component;

import gift.dto.KakaoAuthTokenResponseDto;
import gift.dto.KakaoEmailResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoConnectRestClient implements KakaoConnectClient{

    private final RestClient client = RestClient.builder().build();

    @Value("${REST_API_KEY}")
    private String REST_API_KEY;

    @Override
    public KakaoAuthTokenResponseDto retrieveToken(String code) {
        String requestUrl = "https://kauth.kakao.com/oauth/token";
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

    @Override
    public String getEmail(String token) {
        String requestUrl = "https://kapi.kakao.com/v2/user/me";

        var body = new LinkedMultiValueMap<String, String>();
        body.add("property_keys", "[\"kakao_account.email\"]");

        ResponseEntity<KakaoEmailResponseDto> response = client.post()
                .uri(requestUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(body)
                .retrieve()
                .toEntity(KakaoEmailResponseDto.class);
        KakaoEmailResponseDto result = response.getBody();
        return result.kakao_account().email();
        // todo 예외처리
    }
}
