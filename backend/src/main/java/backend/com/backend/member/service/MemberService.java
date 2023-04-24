package backend.com.backend.member.service;

import backend.com.backend.exception.BusinessLogicException;
import backend.com.backend.exception.ExceptionCode;
import backend.com.backend.member.entity.Member;
import backend.com.backend.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member){
        //이메일 확인
        verifyExistsEmail(member.getEmail());
        return  memberRepository.save(member);
    }



    public Member updateMember(Member member){//추가 해야함
        //유저 존재 유무 확인
        Member findMember = findVerifiedMember(member.getId());

        Optional.ofNullable(member.getFullName())
                .ifPresent(fullName -> findMember.setFullName(fullName));
        Optional.ofNullable(member.getDisplayName())
                .ifPresent(displayName->findMember.setDisplayName(displayName));
        Optional.ofNullable(member.getLocation())
                .ifPresent(location->findMember.setLocation(location));
        Optional.ofNullable(member.getDisplayName())
                .ifPresent(displayName->findMember.setDisplayName(displayName));
        Optional.ofNullable(member.getMemberStatus())
                .ifPresent(memberStatus->findMember.setMemberStatus(memberStatus));

        //추후 수정
//        finduser.getModifiedAt()
        return memberRepository.save(findMember);

    }
    @Transactional(readOnly = true)
    public Member findMember(long userId){

        return findVerifiedMember(userId);
    }
    public Page<Member> findMembers(int page, int size){
        return memberRepository.findAll(PageRequest.of(page,size, Sort.by("Id").descending()));
    }
    public Member deleteMember(long userId){
        Member finduser = findVerifiedMember(userId);
        finduser.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
        return finduser;
    }
    @Transactional(readOnly = true)
    public Member findVerifiedMember(long memberId){
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        //예외 처리 구현하면 추후 수정해야함.
        Member findMember = optionalMember.orElseThrow(()->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findMember;
    }
    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            //예외 처리 구현하면 추후 수정해야함.
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }
}
