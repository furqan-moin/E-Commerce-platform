package com.furqan.ecommerce.repository;

import com.furqan.ecommerce.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

}
