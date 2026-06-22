package cl.duocuc.penalty.controller;

import cl.duocuc.penalty.dto.PenaltyRequestDto;
import cl.duocuc.penalty.dto.PenaltyResponseDto;
import cl.duocuc.penalty.service.IPenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/multas")
@RequiredArgsConstructor
public class PenaltyController {

    private final IPenaltyService service;

    @GetMapping
    public ResponseEntity<List<PenaltyResponseDto>> getAll() {
        List<PenaltyResponseDto> responses = service.findAll();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/usuario/{rut}")
    public ResponseEntity<List<PenaltyResponseDto>> getByRut(@PathVariable("rut") String rut) {
        List<PenaltyResponseDto> responses = service.findByRut(rut);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PenaltyResponseDto> save(@RequestBody PenaltyRequestDto request) {
        PenaltyResponseDto response = service.save(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
