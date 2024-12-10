package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.ApplicationEntity;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
}
