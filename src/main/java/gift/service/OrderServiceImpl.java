package gift.service;

import gift.dto.CreateOrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final WishService wishService;

    private final OptionService optionService;

    private final OrderRepository orderRepository;

    public OrderServiceImpl(
            WishService wishService,
            OptionService optionService,
            OrderRepository orderRepository) {
        this.wishService = wishService;
        this.optionService = optionService;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public OrderResponseDto purchaseProduct(CreateOrderRequestDto requestDto, Long memberId) {
        Option option = optionService.purchaseOption(requestDto.optionId(), requestDto.quantity());
        Order order = new Order(option, requestDto.quantity(), requestDto.message());
        Order savedOrder = orderRepository.save(order);
        wishService.deleteMemberWishByProductIdIfExist(option.getProductId(), memberId);
        return new OrderResponseDto(savedOrder.getId(), option.getId(), savedOrder.getQuantity(),
                savedOrder.getOrderDateTime(), savedOrder.getMessage());
    }
}
