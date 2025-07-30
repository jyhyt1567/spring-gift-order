package gift.service;

import gift.dto.CreateOptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.dto.UpdateOptionQuantityRequestDto;
import gift.entity.Option;
import java.util.List;

public interface OptionService {

    List<OptionResponseDto> findProductOptionById(Long id);

    OptionResponseDto createOption(CreateOptionRequestDto requestDto, Long id);

    OptionResponseDto setOptionQuantity(
            Long id,
            Long optionId,
            UpdateOptionQuantityRequestDto requestDto);

    Option purchaseOption(Long optionId, Long quantity);

    void deleteOption(Long id, Long optionId);

    Option findOptionByIdOrElseThrow(Long id);
}
