package com.eurekaclient.userservice.application;

import com.eurekaclient.userservice.dto.MemberDetailResponseDto;
import com.eurekaclient.userservice.dto.MemberSnsLoginRequestDto;
import com.eurekaclient.userservice.dto.SnsMemberAddRequestDto;
import com.eurekaclient.userservice.dto.TokenResponseDto;

public interface MemberService {

//    void addMember(MemberSaveRequestDto memberSaveRequestDto);

	void snsAddMember(SnsMemberAddRequestDto snsMemberAddRequestDto);

    TokenResponseDto snsLogin(MemberSnsLoginRequestDto memberSnsLoginRequestDto);

	void duplicationEmail(String email);

	MemberDetailResponseDto findMember(String uuid);
}
