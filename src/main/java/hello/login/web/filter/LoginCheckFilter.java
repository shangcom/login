package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList = {"/", "/members/add", "/login", "/logout", "/css/*"};


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        try {
            log.info("인증 체크 필터 시작 [{}]", requestURI);
            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 [{}]", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 [{}]", requestURI);

                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("인증 체크 필터 종료 [{}]", requestURI);

        }


    }

    /**
     * 화이트 리스트의 경우 인증 체크 X
     */
    private boolean isLoginCheckPath(String requestURI) {

/*        와일드카드 패턴을 지원하지 않음: 이 방식은 요청 URI가 화이트리스트의 패턴과 정확히 일치해야 하므로,
          "/css/*"와 같은 와일드카드 패턴을 처리할 수 없음.
          정확한 문자열 비교만 하므로, 경로 패턴 매칭이나 와일드카드 비교가 필요한 경우 적합하지 않음.*/
//        return Arrays.stream(whiteList)
//                .filter(path -> path.equals(requestURI))
//                .findAny().isPresent();

        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
