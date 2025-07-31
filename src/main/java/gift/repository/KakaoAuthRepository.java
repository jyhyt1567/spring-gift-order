package gift.repository;

import gift.entity.KakaoAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoAuthRepository extends JpaRepository<KakaoAuth, Long> {

}
