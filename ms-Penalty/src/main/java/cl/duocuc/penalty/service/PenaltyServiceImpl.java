package cl.duocuc.penalty.service;

import cl.duocuc.penalty.client.UserClient;
import cl.duocuc.penalty.dto.PenaltyRequestDto;
import cl.duocuc.penalty.dto.PenaltyResponseDto;
import cl.duocuc.penalty.dto.external.UserResponseDto;
import cl.duocuc.penalty.model.Penalty;
import cl.duocuc.penalty.repository.IPenaltyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PenaltyServiceImpl implements IPenaltyService {

    private final IPenaltyRepository repository;
    private final UserClient userClient;

    private PenaltyResponseDto toDto(Penalty penalty) {
        return new PenaltyResponseDto(
                penalty.getIdPenalty(),
                penalty.getUserRut(),
                penalty.getAmount(),
                penalty.getReason(),
                penalty.getStatus(),
                penalty.getCreatedAt()
        );
    }

    private Penalty toEntity(PenaltyRequestDto dto) {
        Penalty penalty = new Penalty();
        penalty.setUserRut(dto.getUserRut());
        penalty.setAmount(dto.getAmount());
        penalty.setReason(dto.getReason());
        return penalty;
    }

    @Override
    public PenaltyResponseDto save(PenaltyRequestDto request) {
        log.info("Iniciando validación para cursar multa al usuario RUT: {}", request.getUserRut());

        log.info("Consultando la existencia del usuario mediante UserClient...");
        UserResponseDto user = userClient.getUserByRut(request.getUserRut());
        if (user == null) {
            log.warn("Creación de multa fallida: El usuario RUT {} no fue encontrado en ms-User.", request.getUserRut());
            throw new RuntimeException("usuario no encontrado");
        }

        log.info("Usuario validado. Preparando entidad para registrar la multa de ${} en BD.", request.getAmount());
        Penalty penalty = toEntity(request);
        penalty.setStatus("pendiente");
        penalty.setCreatedAt(LocalDate.now());

        Penalty savedPenalty = repository.save(penalty);
        log.info("Multa guardada exitosamente en la base de datos con estado 'pendiente'. ID: {}", savedPenalty.getIdPenalty());

        return toDto(savedPenalty);
    }

    @Override
    public List<PenaltyResponseDto> findAll() {
        log.info("Consultando la base de datos para recuperar todas las multas.");
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public List<PenaltyResponseDto> findByRut(String rut) {
        log.info("Buscando en la base de datos las multas asociadas al RUT: {}", rut);
        List<Penalty> penalties = repository.findByUserRut(rut);

        if(penalties.isEmpty()) {
            log.warn("Búsqueda fallida: No se encontraron multas o el usuario RUT {} no existe en la BD local.", rut);
            throw new RuntimeException("usuario no encontrado");
        }

        log.info("Transformando {} entidades de multa a DTOs para el RUT: {}", penalties.size(), rut);
        List<PenaltyResponseDto> userList = new ArrayList<>();
        for (Penalty penalty : penalties) {
            userList.add(toDto(penalty));
        }
        return userList;
    }
}