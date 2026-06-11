package com.furqan.ecommerce.service;

import com.furqan.ecommerce.dto.AddressRequestDto;
import com.furqan.ecommerce.dto.AddressResponseDto;
import com.furqan.ecommerce.entity.AddressEntity;
import com.furqan.ecommerce.entity.UserEntity;
import com.furqan.ecommerce.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<AddressEntity> getAllAddresses() {
        return addressRepository.findAll();
    }

    public List<AddressResponseDto> getAddressesByUserId(Long userId) {
        List<AddressEntity> addressEntities = addressRepository.findByUserId(userId);
        if (addressEntities.isEmpty()) {
            throw new RuntimeException("No address found for user : " + userId);
        }
        return addressEntities.stream()
                .map(this::toResponseDto)
                .toList();
    }

    public AddressResponseDto getAddressByAddressId(Long addressId) {
        if (addressId == null) {
            throw new RuntimeException("addressId is required");
        }
        AddressEntity addressEntity = addressRepository.findById(addressId).orElseThrow(() ->
                new RuntimeException("Address not found with address id: " + addressId));
        return toResponseDto(addressEntity);
    }

    public void deleteAddressByAddressId(Long addressId) {
        AddressEntity addressEntity = addressRepository
                .findById(addressId)
                .orElseThrow(() ->
                        new RuntimeException("address not found with address id: " + addressId));
        addressRepository.deleteById(addressId);
    }

    public AddressResponseDto updateAddress(AddressRequestDto addressRequestDto) {
        if (addressRequestDto.getAddressId() == null) {
            throw new RuntimeException("address_id is required to update address");
        }
        AddressEntity addressEntity = addressRepository
                .findById(addressRequestDto.getAddressId())
                .orElseThrow(() ->
                        new RuntimeException("address not found with address id: " + addressRequestDto.getAddressId()));
        applyRequestToEntity(addressRequestDto, addressEntity);
        return toResponseDto(addressRepository.save(addressEntity));

    }

    public AddressResponseDto createAddress(AddressRequestDto addressRequestDto) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setUserId(addressRequestDto.getUserId());
        applyRequestToEntity(addressRequestDto, addressEntity);
        return toResponseDto(addressRepository.save(addressEntity));
    }


    private AddressResponseDto toResponseDto(AddressEntity addressEntity) {
        return AddressResponseDto.builder()
                .addressId(addressEntity.getAddressId())
                .userId(addressEntity.getUserId())
                .addressType(addressEntity.getAddressType())
                .addressLine1(addressEntity.getAddressLine1())
                .addressLine2(addressEntity.getAddressLine2())
                .city(addressEntity.getCity())
                .state(addressEntity.getState())
                .pinCode(addressEntity.getPinCode())
                .country(addressEntity.getCountry())
                .build();
    }

    private void applyRequestToEntity(AddressRequestDto addressRequestDto, AddressEntity addressEntity) {
        addressEntity.setAddressType(addressRequestDto.getAddressType());
        addressEntity.setAddressLine1(addressRequestDto.getAddressLine1());
        addressEntity.setAddressLine2(addressRequestDto.getAddressLine2());
        addressEntity.setCity(addressRequestDto.getCity());
        addressEntity.setState(addressRequestDto.getState());
        addressEntity.setPinCode(addressRequestDto.getPinCode());
        addressEntity.setCountry(addressRequestDto.getCountry());
    }

    public void deleteAddressById(Long id) {
        deleteAddressByAddressId(id);
    }
}