package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        //로그인 하지 않은 사용자도 홈에 접근할 수 있기 때문에 required = false 를 사용

        if (memberId == null) {     //로그인 쿠키(memberId)가 없는 사용자는 기존 home으로 보낸다
            return "home";
        }

        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {      //로그인 쿠키가 있어도, 회원이 없으면 home으로 보낸다
            return "home";
        }

        //로그인 쿠키(memberId)가 있는 사용자는 로그인 사용자 전용 홈 화면인 loginHome으로 보낸다
        model.addAttribute("member", loginMember);  //member데이터 모델에 담아 전달(화원관련정보 출력해야함)
        return "loginHome";
    }


    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
        //세션 관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);

        //로그인
        if (member == null) {   //만약 회원 정보가 없으면, 쿠키나 세션이 없는 것 이므로
            return "home";      //로그인 되지 않은 것으로 처리
        }

        model.addAttribute("member", member);
        return "loginHome";
    }


    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {
        HttpSession session=request.getSession(false);
        //로그인 하지 않을 사용자도 의미없는 세션이 만들어지지않게
        // 세션을 찾아서 사용하는 시점에는 false 옵션을 사용해서 세션을 생성하지 않아야 한다
        if (session == null) {      //세션이 없으면 home
            return "home";
        }

        //로그인 시점에 세션에 보관한 회원 객체를 찾는다
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {      //세션에 회원 데이터가 없으면 home
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3Spring(    //@SessionAttribute로 이미 로그인 된 사용자 찾기
            @SessionAttribute(name=SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }


    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) {

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}