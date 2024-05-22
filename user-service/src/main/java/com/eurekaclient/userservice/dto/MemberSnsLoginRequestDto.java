package com.eurekaclient.userservice.dto;

import com.eurekaclient.userservice.vo.MemberSnsLoginRequestVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSnsLoginRequestDto {

    private String email;
    private String snsType;
    private String snsId;

    public static MemberSnsLoginRequestDto voToDto(MemberSnsLoginRequestVo snsLoginRequestVo) {
        return MemberSnsLoginRequestDto.builder()
            .email(snsLoginRequestVo.getEmail())
            .snsType(snsLoginRequestVo.getSnsType())
            .snsId(snsLoginRequestVo.getSnsId())
            .build();
    }
}
