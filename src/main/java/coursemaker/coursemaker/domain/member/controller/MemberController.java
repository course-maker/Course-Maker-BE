package coursemaker.coursemaker.domain.member.controller;

import coursemaker.coursemaker.domain.member.dto.AccountDto;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value="/signup")
    public String signup(AccountDto accountDto) {

        ModelMapper mapper = new ModelMapper();
        Member member = mapper.map(accountDto, Member.class);
        member.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        memberService.createMember(member);

        return "redirect:/";
    }
}
