package com.eurekaclient.userservice.presentation;

import com.eurekaclient.userservice.application.MemberService;
import com.eurekaclient.userservice.common.SuccessResponse;
import com.eurekaclient.userservice.dto.MemberDetailResponseDto;
import com.eurekaclient.userservice.dto.MemberSnsLoginRequestDto;
import com.eurekaclient.userservice.dto.SnsMemberAddRequestDto;
import com.eurekaclient.userservice.dto.TokenResponseDto;
import com.eurekaclient.userservice.vo.MemberDetailResponseVo;
import com.eurekaclient.userservice.vo.MemberSnsLoginRequestVo;
import com.eurekaclient.userservice.vo.SnsMemberAddRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public SuccessResponse<Object> snsAddMember(
        @RequestBody SnsMemberAddRequestVo snsMemberAddRequestvo) {
        memberService.snsAddMember(SnsMemberAddRequestDto.voToDto(snsMemberAddRequestvo));
        return new SuccessResponse<>(null);
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<Object>> login(
        @RequestBody MemberSnsLoginRequestVo memberSnsLoginRequestVo) {
        TokenResponseDto tokenResponseDto = memberService.snsLogin(
            MemberSnsLoginRequestDto.voToDto(memberSnsLoginRequestVo));

        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, tokenResponseDto.getAccessToken())
            .header("uuid", tokenResponseDto.getUuid())
            .body(new SuccessResponse<>(null));
    }

    @GetMapping("/myprofile")
    public SuccessResponse<MemberDetailResponseVo> memberDetail(@RequestHeader String uuid) {
        return new SuccessResponse<>(MemberDetailResponseDto.dtoToVo(
            memberService.findMember(uuid)));
    }

}
