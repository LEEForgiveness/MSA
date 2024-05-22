package com.eurekaclient.userservice.application;

import com.eurekaclient.userservice.common.exception.CustomException;
import com.eurekaclient.userservice.common.exception.ResponseStatus;
import com.eurekaclient.userservice.common.security.JwtTokenProvider;
import com.eurekaclient.userservice.domain.InterestCategory;
import com.eurekaclient.userservice.domain.Member;
import com.eurekaclient.userservice.domain.SnsInfo;
import com.eurekaclient.userservice.dto.MemberDetailResponseDto;
import com.eurekaclient.userservice.dto.MemberSnsLoginRequestDto;
import com.eurekaclient.userservice.dto.SnsMemberAddRequestDto;
import com.eurekaclient.userservice.dto.TokenResponseDto;
import com.eurekaclient.userservice.infrastructure.InterestCategoryRepository;
import com.eurekaclient.userservice.infrastructure.MemberRepository;
import com.eurekaclient.userservice.infrastructure.SnsInfoRepository;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final SnsInfoRepository snsInfoRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final InterestCategoryRepository interestCategoryRepository;

	//이메일 중복 확인
	@Override
	public void duplicationEmail(String email) {
		if (memberRepository.findByEmail(email).isPresent()) {
			throw new CustomException(ResponseStatus.DUPLICATE_EMAIL);
		}
	}

	private String createToken(Member member) {
		UserDetails userDetails = User.withUsername(member.getEmail()).password(member.getUuid()).roles("USER").build();
		return jwtTokenProvider.generateToken(userDetails);
	}

	//SNS 회원 추가
	@Override
	@Transactional
	public void snsAddMember(SnsMemberAddRequestDto snsMemberAddRequestDto) {
		if (snsInfoRepository.findBySnsIdAndSnsType(snsMemberAddRequestDto.getSnsId(),
			snsMemberAddRequestDto.getSnsType()).isPresent()) {
			throw new CustomException(ResponseStatus.DUPLICATED_MEMBERS);
		}

		duplicationEmail(snsMemberAddRequestDto.getEmail());

		String uuid = UUID.randomUUID().toString();

		String character = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder handle = new StringBuilder("@user-");
		Random random = new Random();
		for (int i = 0; i < 9; i++) {
			handle.append(character.charAt(random.nextInt(character.length())));
		}

		Member member = Member.builder()
			.email(snsMemberAddRequestDto.getEmail())
			.name(snsMemberAddRequestDto.getName())
			.phoneNum(snsMemberAddRequestDto.getPhoneNum())
			.uuid(uuid)
			.handle(handle.toString())
			.build();

		memberRepository.save(member);

		SnsInfo snsInfo = SnsInfo.builder()
			.snsId(snsMemberAddRequestDto.getSnsId())
			.snsType(snsMemberAddRequestDto.getSnsType())
			.member(member)
			.build();

		snsInfoRepository.save(snsInfo);

		// interestCategories 맵에서 각 항목을 가져와 InterestCategory 객체를 생성하고 저장
		Map<Long, String> interestCategories = snsMemberAddRequestDto.getInterestCategories();
		log.info("interestCategories: {}", interestCategories);
		for (Map.Entry<Long, String> category : interestCategories.entrySet()) {
			Long categoryId = category.getKey();
			log.info("categoryId: {}", categoryId);
			String categoryName = category.getValue();
			log.info("categoryName: {}", categoryName);

			InterestCategory interestCategory = InterestCategory.builder()
				.uuid(uuid)
				.categoryId(categoryId)
				.categoryName(categoryName)
				.build();

			interestCategoryRepository.save(interestCategory);
		}
	}

	//	토큰 생성
//	private String createToken(Member member) {
//		UserDetails userDetails = User.withUsername(member.getEmail()).password(member.getUuid())
//			.roles("USER").build();
//		return jwtTokenProvider.generateToken(userDetails);
//	}

	//	소셜 로그인
	@Override
	@Transactional
	public TokenResponseDto snsLogin(MemberSnsLoginRequestDto memberSnsLoginRequestDto) {
        SnsInfo snsInfo = snsInfoRepository.findBySnsIdAndSnsType(memberSnsLoginRequestDto.getSnsId(), memberSnsLoginRequestDto.getSnsType())
                .orElseThrow(() -> new CustomException(ResponseStatus.USER_NOT_FOUND));
		Member member = memberRepository.findByEmail(memberSnsLoginRequestDto.getEmail())
			.orElseThrow(() -> new CustomException(ResponseStatus.USER_NOT_FOUND));
		if (member.isTerminationStatus()) {
			throw new CustomException(ResponseStatus.WITHDRAWAL_MEMBERS);
		}

		String token = createToken(member);

		return TokenResponseDto.builder()
			.accessToken(token)
			.uuid(member.getUuid())
			.build();
	}

	//회원정보 조회
	@Override
	public MemberDetailResponseDto findMember(String uuid) {
		Member member = memberRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ResponseStatus.NO_EXIST_MEMBERS));

		List<InterestCategory> interestCategoryList = interestCategoryRepository.findByUuid(
			member.getUuid());

		List<String> interestCategories = new ArrayList<>();

		for (InterestCategory interestCategory : interestCategoryList) {
			interestCategories.add(interestCategory.getCategoryName());
		}

		return MemberDetailResponseDto.builder()
			.email(member.getEmail())
			.name(member.getName())
			.phoneNum(member.getPhoneNum())
			.handle(member.getHandle())
			.profileImage(member.getProfileImage())
			.watchList(interestCategories)
			.build();
	}

}
