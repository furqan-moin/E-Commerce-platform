package com.furqan.ecommerce.controller;

import com.furqan.ecommerce.dto.*;
import com.furqan.ecommerce.entity.AddressEntity;
import com.furqan.ecommerce.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ecommerce/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/all")
    public List<AddressEntity> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/addressId")
    public AddressResponseDto getAddressById(@RequestHeader("address_id") Long addressId) {
        return addressService.getAddressByAddressId(addressId);
    }

    @PostMapping("/createAddress")
    public AddressResponseDto createAddress(@Valid @RequestBody AddressRequestDto addressRequestDto) {
        return addressService.createAddress(addressRequestDto);
    }

    @DeleteMapping("/deleteAddress")
    public ResponseEntity<ApiResponse> deleteAddressById(@RequestHeader("address_id") Long id) {
        addressService.deleteAddressById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Address deleted successfully")
                .build());
    }

    @PatchMapping("/updateAddress")
    public AddressResponseDto updateAddress(@Valid @RequestBody AddressRequestDto addressRequestDto) {
        return addressService.updateAddress(addressRequestDto.getAddressId(), addressRequestDto);
    }

}
