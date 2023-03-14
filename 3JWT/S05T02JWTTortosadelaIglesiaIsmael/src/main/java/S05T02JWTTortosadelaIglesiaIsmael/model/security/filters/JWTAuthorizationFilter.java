package S05T02JWTTortosadelaIglesiaIsmael.model.security.filters;

import S05T02JWTTortosadelaIglesiaIsmael.model.security.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //System.out.println("començament");
        String bearerToken = request.getHeader("Authorization");
        //System.out.println("bearer token: " + bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.replaceAll("Bearer ", "");
            //System.out.println("token: " + token);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = TokenUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            //System.out.println(usernamePasswordAuthenticationToken.toString());
        }
        filterChain.doFilter(request, response);
    }

}
