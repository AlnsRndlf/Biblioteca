package cl.duocuc.penalty.service;

import cl.duocuc.penalty.client.UserClient;
import cl.duocuc.penalty.dto.PenaltyRequestDto;
import cl.duocuc.penalty.dto.PenaltyResponseDto;
import cl.duocuc.penalty.dto.UserResponseDto;
import cl.duocuc.penalty.model.Penalty;
import cl.duocuc.penalty.repository.IPenaltyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
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
        UserResponseDto user = userClient.getUserByRut(request.getUserRut());
        if (user == null) {
            throw new RuntimeException("usuario no encontrado");
        }
        Penalty penalty = toEntity(request);
        penalty.setStatus("pendiente");
        penalty.setCreatedAt(LocalDate.now());
        Penalty savedPenalty = repository.save(penalty);
        return toDto(savedPenalty);
    }

    @Override
    public List<PenaltyResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public List<PenaltyResponseDto> findByRut(String rut) {
        List<Penalty> penalties = repository.findByUserRut(rut);
        if(penalties.isEmpty()) {
            throw new RuntimeException("usuario no encontrado");
        }
        List<PenaltyResponseDto> userList = new ArrayList<>();
        for (Penalty penalty : penalties) {
            userList.add(toDto(penalty));
        }
        return userList;
    }
}














