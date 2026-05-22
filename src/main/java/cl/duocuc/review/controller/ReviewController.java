package cl.duocuc.review.controller;

import cl.duocuc.review.dto.ReviewRequestDto;
import cl.duocuc.review.dto.ReviewResponseDto;
import cl.duocuc.review.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reseñas") // reseñas no reconcoe la ñ postman
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService service;

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/book/{bookIsbn}")
    public ResponseEntity<List<ReviewResponseDto>> findByBookIsbn(@PathVariable Long bookIsbn) {
        return ResponseEntity.ok(service.findByBookIsbn(bookIsbn));
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> save(@RequestBody ReviewRequestDto request) {
        ReviewResponseDto savedReview = service.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }
}
