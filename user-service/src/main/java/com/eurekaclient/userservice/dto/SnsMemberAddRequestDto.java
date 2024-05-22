package com.eurekaclient.userservice.dto;

import com.eurekaclient.userservice.vo.SnsMemberAddRequestVo;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnsMemberAddRequestDto {

    private String snsId;
    private String snsType;
    private String email;
    private String name;
    private String phoneNum;
    private Map<Long, String> interestCategories;

    public static SnsMemberAddRequestDto voToDto(SnsMemberAddRequestVo snsMemberAddRequestVo) {
        return SnsMemberAddRequestDto.builder()
                .snsId(snsMemberAddRequestVo.getSnsId())
                .snsType(snsMemberAddRequestVo.getSnsType())
                .email(snsMemberAddRequestVo.getEmail())
                .name(snsMemberAddRequestVo.getName())
                .phoneNum(snsMemberAddRequestVo.getPhoneNum())
                .interestCategories(snsMemberAddRequestVo.getInterestCategories())
                .build();
    }
}
