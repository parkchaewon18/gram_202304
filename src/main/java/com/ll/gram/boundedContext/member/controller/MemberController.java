package com.ll.gram.boundedContext.member.controller;

import com.ll.gram.base.rq.Rq;
import com.ll.gram.base.rsData.RsData;
import com.ll.gram.boundedContext.member.entity.Member;
import com.ll.gram.boundedContext.member.service.MemberService;
import com.ll.gram.standard.util.Ut;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "usr/member/join";
    }

    @AllArgsConstructor
    @Getter
    public static class JoinForm {
        @NotBlank
        @Size(min = 4, max = 30)
        private final String username;
        @NotBlank
        @Size(min = 4, max = 30)
        private final String password;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        RsData<Member> joinRs = memberService.join(joinForm.getUsername(), joinForm.getPassword());

        if (joinRs.isFail()) {
            return rq.historyBack(joinRs.getMsg());
        }

        String msg = joinRs.getMsg() + "\n로그인 후 이용해주세요.";

        return "redirect:/member/login?msg=" + Ut.url.encode(msg);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "usr/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String showMe() {
        return "usr/member/me";
    }
}