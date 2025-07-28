package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "kakao_auth")
public class KakaoAuth {

    @Id
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    public KakaoAuth() {
    }

    public KakaoAuth(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void renewalAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void renewalRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
