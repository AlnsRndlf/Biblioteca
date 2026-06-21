package cl.duocuc.review.service;

import cl.duocuc.review.client.BookClient;
import cl.duocuc.review.client.LoanClient;
import cl.duocuc.review.client.UserClient;
import cl.duocuc.review.dto.*;
import cl.duocuc.review.model.Review;
import cl.duocuc.review.repository.IReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {

    private final IReviewRepository repository;
    private final LoanClient loanClient;
    private final UserClient userClient;
    private final BookClient bookClient;

    public ReviewResponseDto toDto(Review review) {
        UserResponseDto userDto = userClient.getUserByRut(review.getUserRut());
        if (userDto == null){
            throw new RuntimeException("el usuario no existe");
        }
        BookResponseDto bookDto = bookClient.getBookByIsbn(review.getBookIsbn());
        if (bookDto == null){
            throw new RuntimeException("el libro no existe");
        }
        return new ReviewResponseDto(
                review.getIdReview(),
                userDto.getUserRut(),
                bookDto.getBookIsbn(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }

    public Review toEntity(ReviewRequestDto reviewDto) {
        Review review = new Review();
        review.setUserRut(reviewDto.getUserRut());
        review.setBookIsbn(reviewDto.getBookIsbn());
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        return   review;
    }

    @Override
    public List<ReviewResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ReviewResponseDto> findByBookIsbn(Long bookIsbn) {
        List<Review> reviews = repository.findByBookIsbn(bookIsbn);
        List<ReviewResponseDto> reviewList=new ArrayList<>();
        for (Review review : reviews) {
            reviewList.add(toDto(review));
        }
        return reviewList;
    }

    @Override
    public ReviewResponseDto findById(Long idReview) {
        Review review = repository.findById(idReview).orElse(null);
        if (review == null){
            throw new RuntimeException("reseña no encontrada");
        }
        return toDto(review);
    }

    @Override
    public ReviewResponseDto save(ReviewRequestDto request) {
        UserResponseDto userDto = userClient.getUserByRut(request.getUserRut());
        if (userDto == null){
            throw new RuntimeException("el usuario no existe");}
        BookResponseDto bookDto = bookClient.getBookByIsbn(request.getBookIsbn());
        if (bookDto == null){
            throw new RuntimeException("el libro no existe");}

        List<LoanResponseDto> userLoans = loanClient.findByUser(request.getUserRut());
        boolean hasBorrowed = false;

        for(LoanResponseDto loan : userLoans){
            if(loan.getBookIsbn().equals(request.getBookIsbn())){
                hasBorrowed = true;
                break;}}

        if (!hasBorrowed){
            throw new RuntimeException("tiene que haber un prestamo para poder hacer una reseña. ");}
        Review review = toEntity(request);
        Review savedReview = repository.save(review);
        return toDto(savedReview);
    }
}