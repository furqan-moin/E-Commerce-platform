package com.furqan.ecommerce.repository;


import com.furqan.ecommerce.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface IAddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findByUserId(Long userId);
    Optional<AddressEntity> findByAddressId(Long addresssId);

}
