package com.company.student.app.controller;

import com.company.student.app.dto.AddressRequest;
import com.company.student.app.dto.AddressResponseDto;
import com.company.student.app.dto.AddressUpdateDto;
import com.company.student.app.dto.HttpApiResponse;
import com.company.student.app.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<HttpApiResponse<Boolean>> createAddress(@RequestBody AddressRequest request) {
        HttpApiResponse<Boolean> response = addressService.createAddress(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<HttpApiResponse<AddressResponseDto>> getById(@RequestParam Long id) {
        HttpApiResponse<AddressResponseDto> response = addressService.getById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping
    public ResponseEntity<HttpApiResponse<Long>> updateAddress(@RequestBody AddressUpdateDto dto) {
        HttpApiResponse<Long> response = addressService.updateAddress(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping
    public ResponseEntity<HttpApiResponse<Boolean>> deleteAddress(@RequestParam Long id) {
        HttpApiResponse<Boolean> response = addressService.deleteAddress(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
