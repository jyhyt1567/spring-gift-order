package gift.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import gift.dto.CreateOrderRequestDto;
import gift.dto.OptionResponseDto;
import gift.dto.OrderResponseDto;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.OrderRepository;
import gift.service.OptionService;
import gift.service.OrderServiceImpl;
import gift.service.WishService;
import java.util.Optional;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private WishService wishService;

    @Mock
    private OptionService optionService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Product product = new Product(1L, "아아", 2_000L, "asd.jpg", null);
    private Option option = new Option(1L, "샷추가", 10L, product);
    private String message = "아아임";
    private Long memberId = 1L;

    @Test
    @DisplayName("구매 성공 테스트")
    void 구매_성공_테스트() {
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto(option.getId(), 2L, message);
        Order order = new Order(1L, option, requestDto.quantity(),message);
        given(optionService.findOptionByIdOrElseThrow(option.getId())).willReturn(option);
        given(optionService.purchaseOption(option.getId(), requestDto.quantity()))
                .willReturn(new OptionResponseDto(
                        option.getId(),
                        option.getName(),
                        option.getQuantity() - requestDto.quantity()));
        given(orderRepository.save(any())).willReturn(order);
        given(wishService.findMemberWishByProductId(product.getId(), memberId))
                .willReturn(Optional.empty());

        OrderResponseDto actual = orderService.purchaseProduct(requestDto, memberId);

        assertAll(
                () -> AssertionsForClassTypes.assertThat(actual.id()).isNotNull(),
                () -> AssertionsForClassTypes.assertThat(actual.message()).isEqualTo(message),
                () -> AssertionsForClassTypes.assertThat(actual.optionId()).isEqualTo(option.getId()),
                () -> AssertionsForClassTypes.assertThat(actual.quantity()).isEqualTo(requestDto.quantity())
        );
    }

    @Test
    @DisplayName("옵션 수량 부족 시 구매 실패 테스트")
    void 수량_부족_구매_실패_테스트() {
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto(option.getId(), 9999L, message);
        given(optionService.findOptionByIdOrElseThrow(option.getId())).willReturn(option);
        given(optionService.purchaseOption(option.getId(), requestDto.quantity()))
                .willThrow(new CustomException(ErrorCode.OptionNotEnough));

        CustomException e = Assertions.assertThrows(CustomException.class,
                () -> orderService.purchaseProduct(requestDto, memberId));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.OptionNotEnough);
    }

    @Test
    @DisplayName("없는 옵션 구매 시 구매 실패 테스트")
    void 없는_옵션_구매_실패_테스트() {
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto(999L, 2L, message);
        given(optionService.findOptionByIdOrElseThrow(999L))
                .willThrow(new CustomException(ErrorCode.OptionNotFound));
        CustomException e = Assertions.assertThrows(CustomException.class,
                () -> orderService.purchaseProduct(requestDto, memberId));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.OptionNotFound);
    }

}
