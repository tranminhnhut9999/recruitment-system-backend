package project.springboot.template.config.security.jwt;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import project.springboot.template.config.security.EscapeUrlConfig;
import project.springboot.template.config.security.jwt.model.UserDetailsImpl;
import project.springboot.template.entity.common.ApiException;
import project.springboot.template.entity.common.ApiResponse;
import project.springboot.template.util.MapperUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUserDetailServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;


    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtFilter(JwtUserDetailServiceImpl userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (EscapeUrlConfig.shouldBypassAuthentication(this.pathMatcher, requestURI, request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(token);
            } catch (Exception e) {
                handleGetUsername(e, response);
                return;
            }
        } else {
            System.out.println("Bearer String not found in token");
            handleGetUsername(ApiException.create(HttpStatus.UNAUTHORIZED).withMessage("Bearer String not found in token"), response);
            return;
        }
        if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
            if (!userDetails.isEnabled()) {
                handleGetUsername(ApiException.create(HttpStatus.FORBIDDEN).withMessage("account is locked"), response);
                return;
            }

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new
                        WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleGetUsername(Exception e, HttpServletResponse response) throws IOException {
        ApiResponse<Object> apiResponse = ApiResponse.failed(e.getMessage());
        response.setContentType("application/json");
        if (e instanceof ApiException) {
            response.setStatus(((ApiException) e).getStatus().value());
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        response.getWriter().write(MapperUtil.getInstance().getMapper().writeValueAsString(apiResponse));
    }


}
