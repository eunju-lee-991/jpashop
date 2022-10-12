package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }
    
    //entity(Member)를 그대로 form으로 받기는 어려움. form 데이터 받는 객체랑 실제 도메인 entity랑 따로 사용하는 것이 좋음
    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result) { //@Valid 객체에 있는 validation 쓸 수 있게 해줌 ex. @NotEmpty

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) { // 실무에서는 member 객체 그대로 보여주는 것보다 DTO 따로 만드는 것이 좋음
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
        // API에서는 절대 엔티티를 그대로 보내면 안됨! 스펙이 달라지기 때문
    }
}
