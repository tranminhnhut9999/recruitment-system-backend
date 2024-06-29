package project.springboot.template.config.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.springboot.template.config.security.TokenHolder;
import project.springboot.template.entity.common.ApiException;
import project.springboot.template.entity.common.ApiResponse;
import project.springboot.template.service.HttpService;
import project.springboot.template.util.MapperUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final HttpService httpService;

    private static final List<String> escapeURLs =
            new ArrayList<>(Arrays.asList("/api/jobs/hiring", "/api/jobs/hiring/*", "api-docs/swagger-config", "api-docs", "swagger-ui"));


    public JwtFilter(JwtUtil jwtUtil, HttpService httpService) {
        this.jwtUtil = jwtUtil;
        this.httpService = httpService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (isURLMatched(requestURI)) {
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
                TokenHolder.setToken(token);
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
            ApiResponse<Boolean> res = this.httpService.get("http://localhost:8081/api/accounts/verify", new HashMap<>(), token);
            if (res.getData()) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.emptyList());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
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
    private boolean isURLMatched(String url) {
        return escapeURLs.stream().anyMatch(pattern -> {
            if (pattern.equals(url)) {
                return true;
            } else if (pattern.endsWith("/*")) {
                String basePattern = pattern.substring(0, pattern.length() - 2);
                return url.startsWith(basePattern);
            } else {
                return false;
            }
        });
    }
}
