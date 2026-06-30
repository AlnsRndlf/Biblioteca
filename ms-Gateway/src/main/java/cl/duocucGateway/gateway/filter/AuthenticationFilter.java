package cl.duocucGateway.gateway.filter;

import cl.duocucGateway.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String method = request.getMethod().name();

        // 1. Dejar pasar las peticiones públicas (Login y Registro)
        if (path.contains("/api/v1/auth")) {
            return chain.filter(exchange);
        }

        // 2. Validar que la petición traiga el Header de Authorization
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            log.warn("Acceso denegado: Falta el token de autorización en la petición.");
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7); // Quitamos la palabra "Bearer "
        } else {
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        try {
            // 3. Validar si el token es real y no ha expirado
            jwtUtil.validateToken(authHeader);

            // 4. Extraer el rol del token (ROLE_USER o ROLE_ADMIN)
            Claims claims = jwtUtil.getClaims(authHeader);
            String role = claims.get("role", String.class);
            log.info("Token válido en Gateway. Rol: {}, Petición: {} {}", role, method, path);

            // REGLA 1: Solo el ADMIN puede agregar, editar o eliminar Libros y Usuarios
            boolean isBookOrUserModification = (path.contains("/api/v1/libros") || path.contains("/api/v1/usuarios"))
                    && (method.equals("POST") || method.equals("PATCH") || method.equals("DELETE") || method.equals("PUT"));

            if (isBookOrUserModification && !"ROLE_ADMIN".equals(role)) {
                log.warn("RBAC Denegado: Un {} intentó modificar registros ({}).", role, path);
                return onError(exchange, HttpStatus.FORBIDDEN); // 403 Forbidden
            }

            // REGLA 2: Solo el ADMIN puede cursar multas directamente
            boolean isPenaltyCreation = path.contains("/api/v1/multas") && method.equals("POST");
            if (isPenaltyCreation && !"ROLE_ADMIN".equals(role)) {
                log.warn("RBAC Denegado: Un {} intentó crear una multa.", role);
                return onError(exchange, HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            return onError(exchange, HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        // Si pasa todas las validaciones, el Gateway envía la petición al microservicio
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // Alta prioridad, se ejecuta antes de enrutar
    }
}