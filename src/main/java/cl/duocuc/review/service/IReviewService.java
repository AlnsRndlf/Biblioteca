package cl.duocuc.review.service;


import cl.duocuc.review.dto.ReviewRequestDto;
import cl.duocuc.review.dto.ReviewResponseDto;

import java.util.List;

public interface IReviewService {
    ReviewResponseDto save(ReviewRequestDto request);
    List<ReviewResponseDto> findAll();
    List<ReviewResponseDto> findByBookIsbn(Long bookIsbn);
}
