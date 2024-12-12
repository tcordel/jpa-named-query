package com.example.demo.repositories.custom.impl;

import com.example.demo.entities.TypeNotificationEntity;
import com.example.demo.entities.UserNotifEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.custom.CustomUserNotifRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;

public class CustomUserNotifRepositoryImpl implements CustomUserNotifRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void criteriaDelete(UserEntity user, TypeNotificationEntity typeNofif) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<UserNotifEntity> criteriaQuery = criteriaBuilder.createCriteriaDelete(UserNotifEntity.class);
		Root<UserNotifEntity> root = criteriaQuery.from(UserNotifEntity.class);
		ParameterExpression<UserEntity> createUserParameter = criteriaBuilder.parameter(UserEntity.class);
		ParameterExpression<TypeNotificationEntity> createTypeNotifParameter = criteriaBuilder
				.parameter(TypeNotificationEntity.class);
		Query query = entityManager
				.createQuery(criteriaQuery.where(
						criteriaBuilder.and(
								criteriaBuilder.equal(root.get("user"), createUserParameter),
								criteriaBuilder.equal(root.get("typeNotif"), createTypeNotifParameter))));
		query.setParameter(createUserParameter, user);
		query.setParameter(createTypeNotifParameter, typeNofif);
		query.executeUpdate();
	}
}

