package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    /**
     * @return null이면 로그인 실패
     */
    public Member login(String loginId, String password) {
        //회원을 조회한 다음에
        Optional<Member> findMemberOptional=memberRepository.findByLoginId(loginId);
        Member member=findMemberOptional.get();

        if (member.getPassword().equals(password)){
            return member;  // 파라미터로 넘어온 password와 비교해서 같으면 회원을 반환
        }else {
            return null;    // 만약 password가 다르면 null 을 반환
        }

        /** == */
        /*return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);*/
    }
}
