package project.transfi.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.transfi.entity.User;
import project.transfi.service.CustomUserDetails;
import project.transfi.utill.JwtUtill;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtill jwtUtill;
    private final CustomUserDetails customUserDetails;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (token.isBlank()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } else {
                try {
                    Map<String, String> claims = jwtUtill.validateToken(token);
                    String username = claims.get("username");

                    if (username == null) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Username not found in token");
                        return;
                    }
                    User user = customUserDetails.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid or expired JWT token");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

