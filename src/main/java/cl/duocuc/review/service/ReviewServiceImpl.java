package cl.duocuc.review.service;

import cl.duocuc.review.client.LoanClient;
import cl.duocuc.review.dto.ReviewResponseDto;
import cl.duocuc.review.model.Review;
import cl.duocuc.review.repository.IReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {

    private final IReviewRepository repository;

    private final LoanClient loanClient;

    //public ReviewResponseDto toDto(Review review) {}
}
