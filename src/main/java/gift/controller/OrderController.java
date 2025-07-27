package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.CreateOrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Member;
import gift.service.KakaoLoginService;
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

    private final KakaoLoginService kakaoLoginService;

    public OrderController(OrderService orderService, KakaoLoginService kakaoLoginService) {
        this.orderService = orderService;
        this.kakaoLoginService = kakaoLoginService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> orderProduct(
            @Valid @RequestBody CreateOrderRequestDto requestDto,
            @RequestHeader("Authorization") String token
    ) {
        Member member = kakaoLoginService.isValidateUser(token);
        Long memberId = member.getId();
        OrderResponseDto orderResponseDto = orderService.purchaseProduct(requestDto, memberId);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

}
