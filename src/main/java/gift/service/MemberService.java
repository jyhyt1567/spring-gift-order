package gift.service;

import gift.dto.CreateMemberRequestDto;
import gift.dto.DeleteMemberRequestDto;
import gift.dto.JWTResponseDto;
import gift.dto.UpdateMemberPasswordRequestDto;
import gift.entity.Member;

public interface MemberService {

    JWTResponseDto createMember(CreateMemberRequestDto requestDto);

    JWTResponseDto loginMember(CreateMemberRequestDto requestDto);

    void updateMemberPassword(UpdateMemberPasswordRequestDto requestDto);

    void deleteMember(DeleteMemberRequestDto requestDto);

    Member findMemberByEmailOrElseThrow(String email);
}
