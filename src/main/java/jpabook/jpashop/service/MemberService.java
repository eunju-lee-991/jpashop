package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@AllArgsConstructor // 모든 필드에 대한 생성자 만들어줌
@RequiredArgsConstructor // final 붙은 필드에 대한 생성자 만들어줌
@Service
@Transactional(readOnly = true) // readOnly = true 넣으면 조회 성능 최적화
 // spring과 javax가 제공하는 transactional이 있는데 스프링 거 쓰는 게 나음
public class MemberService {

    private final MemberRepository memberRepository;

    // @Autowired 최신 스프링에서는 생성자 1개인 경우 알아서 인젝션 해줌
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원가입
     */
    @Transactional // readonly false
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());

        //Exception
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("you already exist");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) { // member를 반환하지 않는 이유. commmand와 query를 구분. member반환하면 업데이트 command에서 조회를 하는 거니까??
        Member member = memberRepository.findOne(id); // 변경 감지. 영속성 컨텍스트에서 member 찾음
        member.setName(name);
    }
}
