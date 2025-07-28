package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.CreateOrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.KakaoAuth;
import gift.entity.Member;
import gift.service.KakaoService;
import gift.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    private final KakaoService kakaoService;

    public OrderController(OrderService orderService, KakaoService kakaoService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> orderProduct(
            @Valid @RequestBody CreateOrderRequestDto requestDto,
            @LoginMember Member member
    ) {
        kakaoService.isValidateUser(member);
        Long memberId = member.getId();
        OrderResponseDto orderResponseDto = orderService.purchaseProduct(requestDto, memberId);
        kakaoService.sendMessage(orderResponseDto, member);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

}
