package com.eurekaclient.userservice.vo;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class MemberDetailResponseVo {

	private String email;
	private String name;
	private String phoneNum;
	private String handle;
	private String profileImage;
	private List<String> watchList;

	public MemberDetailResponseVo(String email, String name, String phoneNum,
		String handle, String profileImage, List<String> watchList) {
		this.email = email;
		this.name = name;
		this.phoneNum = phoneNum;
		this.handle = handle;
		this.profileImage = profileImage;
		this.watchList = watchList;
	}
}
