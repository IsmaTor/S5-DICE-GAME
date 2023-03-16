package ismaelTortosa.diceGame.model.security.filters;

import ismaelTortosa.diceGame.model.security.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken, token;

        bearerToken = request.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer")){
            token = bearerToken.replaceAll("Bearer ", "");
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = TokenUtils.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
