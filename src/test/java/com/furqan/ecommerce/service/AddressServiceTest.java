package com.furqan.ecommerce.service;

import com.furqan.ecommerce.exception.AddressNotFoundException;
import com.furqan.ecommerce.repository.IAddressRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddressServiceTest {

    private final IAddressRepository addressRepository = mock(IAddressRepository.class);
    private final AddressService addressService = new AddressService(addressRepository);

    @Test
    void missingAddressUsesDomainException() {
        when(addressRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class,
                () -> addressService.getAddressByAddressId(42L));
    }

    @Test
    void missingUserIdUsesBadRequestException() {
        assertThrows(IllegalArgumentException.class,
                () -> addressService.getAddressesByUserId(null));
    }
}
