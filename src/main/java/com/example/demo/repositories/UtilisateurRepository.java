package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.UtilisateurEntity;

public interface UtilisateurRepository extends JpaRepository<UtilisateurEntity, Long> {
}
