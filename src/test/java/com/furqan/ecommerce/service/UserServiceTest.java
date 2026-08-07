package com.furqan.ecommerce.service;

import com.furqan.ecommerce.dto.user.UserRequestDto;
import com.furqan.ecommerce.dto.user.UserResponseDto;
import com.furqan.ecommerce.entity.UserEntity;
import com.furqan.ecommerce.enums.Role;
import com.furqan.ecommerce.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUserAppliesSchemaCompatibleDefaults() {
        UserRequestDto request = new UserRequestDto();
        request.setFirstName("Test");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setCountryCode("+91");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            user.setUserId(1L);
            return user;
        });

        UserResponseDto response = userService.createUser(request);

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals(Role.CUSTOMER, userCaptor.getValue().getRole());
        assertTrue(userCaptor.getValue().getEnabled());
        assertEquals(Role.CUSTOMER.name(), response.getRole());
        assertTrue(response.getEnabled());
    }
}
