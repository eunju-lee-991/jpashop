package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController // controller + responseBody
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findMembes = memberService.findMembers();
        List<MemberDto> collect = findMembes.stream().map(m -> new MemberDto(m.getName())).collect(Collectors.toList());

        return new Result(collect.size(), collect); // List로 바로 반환하면 json 배열으로 나가서 유연성이 떨어짐..?
        // 이렇게 하면 껍데기 object고 data 안에 배열. count 같은 거 추가 가능(data와 같은 계층)
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto { // DTO 사용하ㅕㄴ Entity 변경돼도 api 스펙이 변하지 않음
        private String name;
    }

    //@RequestBody하면 JSON으로 온 데이터를 Member에 매핑해줌
    //@Valid 사용하면 javax validation 관련하여 엔티티 검증
    //Member Entity를 그대로 파라미터로 받으면 안되고 DTO 만들어야함 -> v2
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        //return id;
        return new CreateMemberResponse(id);
    }

    // 파라미터 엔티티로 안 받고 DTO로 받아야함 (엔티티 노출X)
    // 그래야 엔티티와 API 스펙 분리
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());
        member.setAddress(request.getAddress());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(id, findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
        private Address address;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
