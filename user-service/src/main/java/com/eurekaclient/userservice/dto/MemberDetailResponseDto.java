package com.eurekaclient.userservice.dto;

import com.eurekaclient.userservice.vo.MemberDetailResponseVo;
import java.util.List;
import java.util.Map;
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
public class MemberDetailResponseDto {

	private String email;
	private String name;
	private String phoneNum;
	private String handle;
	private String profileImage;
	private List<String> watchList;

	public static MemberDetailResponseVo dtoToVo(MemberDetailResponseDto memberDetailResponseDto) {
		return new MemberDetailResponseVo(
			memberDetailResponseDto.getEmail(),
			memberDetailResponseDto.getName(),
			memberDetailResponseDto.getPhoneNum(),
			memberDetailResponseDto.getHandle(),
			memberDetailResponseDto.getProfileImage(),
			memberDetailResponseDto.getWatchList());
	}
}
