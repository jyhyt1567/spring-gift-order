package gift.interceptor;

import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.service.KakaoService;
import gift.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CustomAuthInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    private final KakaoService kakaoService;

    public CustomAuthInterceptor(TokenService tokenService, KakaoService kakaoService) {
        this.tokenService = tokenService;
        this.kakaoService = kakaoService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        if (request.getRequestURI().startsWith("/api/products") && request.getMethod()
                .equals("GET")) {
            return true;
        }
        String token = request.getHeader("Authorization");
        Member find;
        try {
            find = kakaoService.isValidateUser(token);
        } catch (Exception e) {
            try {
                find = tokenService.isValidateToken(token);
            } catch (Exception e2) {
                throw new CustomException(ErrorCode.NotRegisterd);
            }
        }
        request.setAttribute("login", find);
        return true;
    }
}
