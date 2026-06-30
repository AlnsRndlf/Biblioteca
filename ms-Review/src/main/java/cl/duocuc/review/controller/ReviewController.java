package cl.duocuc.review.controller;

import cl.duocuc.review.dto.ReviewRequestDto;
import cl.duocuc.review.dto.ReviewResponseDto;
import cl.duocuc.review.service.IReviewService;
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
public class ReviewController {

    private final IReviewService service;

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> findAll() {
        log.info("Solicitud recibida para obtener todas las reseñas del sistema.");
        List<ReviewResponseDto> reviews = service.findAll();
        log.info("Se retornaron {} reseñas en total.", reviews.size());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/libro/{bookIsbn}")
    public ResponseEntity<List<ReviewResponseDto>> findByBookIsbn(@PathVariable Long bookIsbn) {
        log.info("Solicitud recibida para obtener las reseñas del libro ISBN: {}", bookIsbn);
        List<ReviewResponseDto> bookReviews = service.findByBookIsbn(bookIsbn);
        log.info("Se encontraron {} reseñas para el libro ISBN: {}", bookReviews.size(), bookIsbn);
        return ResponseEntity.ok(bookReviews);
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> save(@RequestBody ReviewRequestDto request) {
        log.info("Solicitud recibida para registrar una reseña. Usuario RUT: {}, Libro ISBN: {}, Calificación: {}",
                request.getUserRut(), request.getBookIsbn(), request.getRating());
        ReviewResponseDto savedReview = service.save(request);
        log.info("Reseña registrada exitosamente respondiendo al cliente. ID Reseña: {}", savedReview.idReview());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }
}