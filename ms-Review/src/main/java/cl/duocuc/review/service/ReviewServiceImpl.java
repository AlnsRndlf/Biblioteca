package cl.duocuc.review.service;

import cl.duocuc.review.client.BookClient;
import cl.duocuc.review.client.LoanClient;
import cl.duocuc.review.client.UserClient;
import cl.duocuc.review.dto.*;
import cl.duocuc.review.dto.external.BookResponseDto;
import cl.duocuc.review.dto.external.LoanResponseDto;
import cl.duocuc.review.dto.external.UserResponseDto;
import cl.duocuc.review.model.Review;
import cl.duocuc.review.repository.IReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements IReviewService {

    private final IReviewRepository repository;
    private final LoanClient loanClient;
    private final UserClient userClient;
    private final BookClient bookClient;

    public ReviewResponseDto toDto(Review review) {
        UserResponseDto userDto = userClient.getUserByRut(review.getUserRut());
        if (userDto == null){
            log.error("Error al construir DTO: El usuario RUT {} no existe en ms-User.", review.getUserRut());
            throw new RuntimeException("el usuario no existe");
        }

        BookResponseDto bookDto = bookClient.getBookByIsbn(review.getBookIsbn());
        if (bookDto == null){
            log.error("Error al construir DTO: El libro ISBN {} no existe en ms-Book.", review.getBookIsbn());
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
        return review;
    }

    @Override
    public List<ReviewResponseDto> findAll() {
        log.info("Consultando la base de datos para obtener todas las reseñas.");
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ReviewResponseDto> findByBookIsbn(Long bookIsbn) {
        log.info("Buscando en BD las reseñas asociadas al libro ISBN: {}", bookIsbn);
        List<Review> reviews = repository.findByBookIsbn(bookIsbn);

        log.info("Transformando {} entidades de reseña a DTOs para el ISBN: {}", reviews.size(), bookIsbn);
        List<ReviewResponseDto> reviewList = new ArrayList<>();
        for (Review review : reviews) {
            reviewList.add(toDto(review));
        }
        return reviewList;
    }

    @Override
    public ReviewResponseDto findById(Long idReview) {
        log.info("Buscando en BD la reseña con ID: {}", idReview);
        Review review = repository.findById(idReview).orElse(null);
        if (review == null){
            log.warn("Búsqueda fallida: La reseña ID {} no existe.", idReview);
            throw new RuntimeException("reseña no encontrada");
        }
        log.info("Reseña ID {} encontrada exitosamente.", idReview);
        return toDto(review);
    }

    @Override
    public ReviewResponseDto save(ReviewRequestDto request) {
        log.info("Iniciando validaciones complejas para crear reseña. RUT: {}, ISBN: {}", request.getUserRut(), request.getBookIsbn());

        log.info("Paso 1: Validando existencia del usuario mediante UserClient...");
        UserResponseDto userDto = userClient.getUserByRut(request.getUserRut());
        if (userDto == null){
            log.warn("Validación fallida: Usuario RUT {} no encontrado.", request.getUserRut());
            throw new RuntimeException("el usuario no existe");
        }

        log.info("Paso 2: Validando existencia del libro mediante BookClient...");
        BookResponseDto bookDto = bookClient.getBookByIsbn(request.getBookIsbn());
        if (bookDto == null){
            log.warn("Validación fallida: Libro ISBN {} no encontrado.", request.getBookIsbn());
            throw new RuntimeException("el libro no existe");
        }

        log.info("Paso 3: Validando si el usuario RUT {} solicitó previamente el libro ISBN {} mediante LoanClient...", request.getUserRut(), request.getBookIsbn());
        List<LoanResponseDto> userLoans = loanClient.getUserByRut(request.getUserRut());
        boolean hasBorrowed = false;

        for(LoanResponseDto loan : userLoans){
            if(loan.getBookIsbn().equals(request.getBookIsbn())){
                hasBorrowed = true;
                break;
            }
        }

        if (!hasBorrowed){
            log.warn("Validación fallida: El usuario RUT {} intentó reseñar el libro ISBN {} sin haberlo pedido prestado antes.", request.getUserRut(), request.getBookIsbn());
            throw new RuntimeException("tiene que haber un prestamo para poder hacer una reseña. ");
        }

        log.info("Todas las validaciones superadas. Guardando reseña en la base de datos local...");
        Review review = toEntity(request);
        Review savedReview = repository.save(review);
        log.info("Reseña guardada exitosamente con ID: {}", savedReview.getIdReview());

        return toDto(savedReview);
    }
}