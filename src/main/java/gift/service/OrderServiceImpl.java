package gift.service;

import gift.component.KakaoConnectClient;
import gift.dto.CreateOrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.exception.CustomException;
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
        Option option = optionService.findOptionById(requestDto.optionId());
        optionService.purchaseOption(option.getId(), requestDto.quantity());
        Product product = option.getProduct();
        Order order = orderRepository.save(
                new Order(option, requestDto.quantity(), requestDto.message()));
        if (wishService.findMemberWishByProductId(product.getId(), memberId).isPresent()) {
            wishService.deleteMemberWishByProductId(product.getId(), memberId);
        }
        return new OrderResponseDto(order.getId(), option.getId(), order.getQuantity(),
                order.getOrderDateTime(), order.getMessage());
    }
}
