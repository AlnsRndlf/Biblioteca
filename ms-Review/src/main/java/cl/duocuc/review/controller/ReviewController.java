package cl.duocuc.review.controller;

import cl.duocuc.review.dto.ReviewRequestDto;
import cl.duocuc.review.dto.ReviewResponseDto;
import cl.duocuc.review.service.IReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resenas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reseñas", description = "Endpoints para la publicación y consulta de opiniones sobre los libros")
public class ReviewController {

    private final IReviewService service;

    @Operation(summary = "Obtener todas las reseñas", description = "Obtiene un listado general con todas las reseñas escritas en la plataforma.")
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> findAll() {
        log.info("Solicitud recibida para obtener todas las reseñas del sistema.");
        List<ReviewResponseDto> reviews = service.findAll();
        log.info("Se retornaron {} reseñas en total.", reviews.size());
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Buscar reseñas de un libro", description = "Filtra y devuelve todas las reseñas y calificaciones asociadas a un libro específico.")
    @GetMapping("/libro/{bookIsbn}")
    public ResponseEntity<List<ReviewResponseDto>> findByBookIsbn(
            @Parameter(description = "ISBN del libro consultado", required = true) @PathVariable Long bookIsbn) {
        log.info("Solicitud recibida para obtener las reseñas del libro ISBN: {}", bookIsbn);
        List<ReviewResponseDto> bookReviews = service.findByBookIsbn(bookIsbn);
        log.info("Se encontraron {} reseñas para el libro ISBN: {}", bookReviews.size(), bookIsbn);
        return ResponseEntity.ok(bookReviews);
    }

    @Operation(summary = "Publicar una reseña", description = "Permite a un usuario calificar un libro, siempre y cuando lo haya pedido prestado previamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reseña publicada con éxito"),
            @ApiResponse(responseCode = "400", description = "El usuario no existe, el libro no existe, o no registra préstamos previos de este libro")
    })
    @PostMapping
    public ResponseEntity<ReviewResponseDto> save(@RequestBody ReviewRequestDto request) {
        log.info("Solicitud recibida para registrar una reseña. Usuario RUT: {}, Libro ISBN: {}, Calificación: {}",
                request.getUserRut(), request.getBookIsbn(), request.getRating());
        ReviewResponseDto savedReview = service.save(request);
        log.info("Reseña registrada exitosamente respondiendo al cliente. ID Reseña: {}", savedReview.getIdReview());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }
}