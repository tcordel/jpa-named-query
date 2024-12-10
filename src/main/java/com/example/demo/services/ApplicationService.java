package com.example.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.ApplicationEntity;
import com.example.demo.repositories.ApplicationRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationService {

	private final ApplicationRepository applicationRepository;

	public ApplicationEntity save(ApplicationEntity applicationEntity) {
		return applicationRepository.save(applicationEntity);
	}
}
