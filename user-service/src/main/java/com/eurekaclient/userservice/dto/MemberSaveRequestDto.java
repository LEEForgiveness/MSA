package com.eurekaclient.userservice.dto;

import com.eurekaclient.userservice.vo.MemberSaveRequestVo;
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
public class MemberSaveRequestDto {

	private String name;
	private String email;
	private String phoneNum;

	public static MemberSaveRequestDto voToDto(
		MemberSaveRequestVo memberSaveRequestVo) {
		return MemberSaveRequestDto.builder()
			.name(memberSaveRequestVo.getName())
			.email(memberSaveRequestVo.getEmail())
			.phoneNum(memberSaveRequestVo.getPhoneNum())
			.build();
	}

}
