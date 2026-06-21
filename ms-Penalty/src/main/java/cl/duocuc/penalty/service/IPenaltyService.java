package cl.duocuc.penalty.service;

import cl.duocuc.penalty.dto.PenaltyRequestDto;
import cl.duocuc.penalty.dto.PenaltyResponseDto;

import java.util.List;

public interface IPenaltyService {

    PenaltyResponseDto save(PenaltyRequestDto penalty);
    List<PenaltyResponseDto> findAll();
    List<PenaltyResponseDto> findByRut(String rut);
}
