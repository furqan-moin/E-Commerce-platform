package com.furqan.ecommerce.entity;

import com.furqan.ecommerce.enums.AddressType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NonNull
    private Long userId;
    @NonNull
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    @NotBlank
    private String addressLine1;
    private String addressLine2;
    @NotBlank
    private String city;
    @NotBlank
    private String state;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @NotBlank
    private String pinCode;
    @NotBlank
    private String country;


}