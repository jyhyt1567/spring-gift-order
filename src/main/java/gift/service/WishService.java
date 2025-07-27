package gift.service;

import gift.dto.CreateWishRequestDto;
import gift.dto.WishPageDto;
import gift.dto.WishResponseDto;

import gift.entity.Wish;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishService {

    WishResponseDto createWish(CreateWishRequestDto requestDto, Long memberId);

    WishPageDto findMemberWishes(Long memberId, Pageable pageable);

    WishResponseDto updateMemberWishQuantityByProductId(Long quantity, Long productId,
            Long memberId);

    void deleteMemberWishByProductId(Long productId, Long memberId);

    Optional<Wish> findMemberWishByProductId(Long productId, Long memberId);
}