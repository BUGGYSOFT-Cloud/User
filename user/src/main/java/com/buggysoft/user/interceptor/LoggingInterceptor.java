package com.buggysoft.user.interceptor;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.logging.Logger;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
  private static final Logger logger = Logger.getLogger(LoggingInterceptor.class.getName());
  private static final String TOKEN_PREFIX = "Bearer ";
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    logger.info("Incoming request: " + request.getMethod() + " " + request.getRequestURI());
    Enumeration<String> headers = request.getHeaderNames();
    while (headers.hasMoreElements()) {
      String header = headers.nextElement();
      logger.info("Header: " + header + " = " + request.getHeader(header));
    }
    String uri = request.getRequestURI();
    if (uri.equals("/login") || uri.equals("/register") || uri.equals("/saveUser")) {
      logger.info("Skipping token validation for: " + uri);
      return true;
    }
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
      logger.warning("Missing or invalid Authorization header");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Missing or invalid Authorization header");
      return false;
    }

    String token = authHeader.substring(TOKEN_PREFIX.length());
    String email = validateToken(token);
    if (email == null) {
      logger.warning("Invalid token for request to: " + uri);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid token");
      return false;
    }

    request.setAttribute("email", email);
    logger.info("Valid token from " + email + " for request to: " + uri);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    logger.info("Response status: " + response.getStatus());
    logger.info("Completed processing request for " + request.getMethod() + " " + request.getRequestURI());
  }

  private String validateToken(String token) {
    String SECRET_KEY = System.getenv("JWT_SECRET_KEY");;
    Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    try {
      JWTVerifier verifier = JWT.require(ALGORITHM).build();
      DecodedJWT jwt = verifier.verify(token);
      String email = jwt.getSubject();
      return email;
    } catch (TokenExpiredException e) {
      logger.warning("Token has expired: " + e.getMessage());
    } catch (JWTVerificationException e) {
      logger.warning("Token verification failed: " + e.getMessage());
    } catch (Exception e) {
      logger.warning("Unexpected error during token validation: " + e.getMessage());
    }
    return null;
  }
}