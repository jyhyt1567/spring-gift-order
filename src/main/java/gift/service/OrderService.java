package gift.service;

import gift.dto.CreateOrderRequestDto;
import gift.dto.OrderResponseDto;

public interface OrderService {

    OrderResponseDto purchaseProduct(CreateOrderRequestDto requestDto, Long memberId);
}
