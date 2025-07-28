package gift.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kakao")
public class Properties {

    private String restApiKey;
    private String authUrl;
    private String apiUrl;
    private String redirectUri;

    public Properties() {
    }

    public Properties(String restApiKey, String authUrl, String apiUrl, String redirectUri) {
        this.restApiKey = restApiKey;
        this.authUrl = authUrl;
        this.apiUrl = apiUrl;
        this.redirectUri = redirectUri;
    }

    public String getRestApiKey() {
        return restApiKey;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRestApiKey(String restApiKey) {
        this.restApiKey = restApiKey;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
