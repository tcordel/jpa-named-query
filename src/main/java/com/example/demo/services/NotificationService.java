package com.example.demo.services;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.TypeNotificationEntity;
import com.example.demo.entities.UserNotifAppliEntity;
import com.example.demo.entities.UserNotifEntity;
import com.example.demo.entities.UtilisateurEntity;
import com.example.demo.repositories.UserNotifAppliRepository;
import com.example.demo.repositories.UserNotifRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
	private final UserNotifRepository userNotifRepository;
	private final UserNotifAppliRepository userNotifAppliRepository;
	private final EntityManagerFactory entityManagerFactory;

	public Map<TypeNotificationEntity, List<UserNotifAppliEntity>> makeAppNotifMapFromUserNotif(
			UtilisateurEntity user) {
		Map<TypeNotificationEntity, List<UserNotifAppliEntity>> userNotifMap = new HashMap<>();
		List<UserNotifAppliEntity> thisMakesItBug = userNotifAppliRepository.findAll();
		for (UserNotifEntity userNotif : userNotifRepository.findAllByUser(user)) {
			userNotifMap.put(userNotif.getTypeNotif(), null);
		}
		return userNotifMap;
	}

	public void unsubscribeWithoutTransactional(UtilisateurEntity user) {
		for (TypeNotificationEntity typeNotif : getTypeNotifsForbidden(user)) {
			userNotifRepository.deleteAllByUserAndTypeNotif(user, typeNotif);
		}
	}

	private List<TypeNotificationEntity> getTypeNotifsForbidden(UtilisateurEntity user) {
		List<TypeNotificationEntity> typeNotifsForbidden = new ArrayList<>();
		for (Entry<TypeNotificationEntity, List<UserNotifAppliEntity>> entry : makeAppNotifMapFromUserNotif(user)
				.entrySet()) {
			typeNotifsForbidden.add(entry.getKey());
		}
		return typeNotifsForbidden;
	}

	@Transactional
	public void unsubscribeWithTransactional(UtilisateurEntity user) {
		for (TypeNotificationEntity typeNotif : getTypeNotifsForbidden(user)) {
			userNotifRepository.deleteAllByUserAndTypeNotif(user, typeNotif);
		}
		log.error("@@@ End");
	}

	@Transactional
    public void unsubscribeWithTransactionalUsingJpqlForDeletion(UtilisateurEntity user) {
		for (TypeNotificationEntity typeNotif : getTypeNotifsForbidden(user)) {
			userNotifRepository.jpqlDelete(user, typeNotif);
		}
    }

	@Transactional
	public void unsubscribeWithTransactionRequiresNewOnDelete(UtilisateurEntity user) {
		for (TypeNotificationEntity typeNotif : getTypeNotifsForbidden(user)) {
			((NotificationService) AopContext.currentProxy()).removeTypeNotifWithNewTransaction(user, typeNotif);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void removeTypeNotifWithNewTransaction(UtilisateurEntity pkUser, TypeNotificationEntity pkTypeNotif) {
		userNotifRepository.deleteAllByUserAndTypeNotif(pkUser, pkTypeNotif);
	}

	public void removeNotificationUsingEntityManagerFactory(UtilisateurEntity user) {
		log.error("@@@ Start");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UserNotifEntity> userNotifCriteria = criteriaBuilder.createQuery(UserNotifEntity.class);
		CriteriaDelete<UserNotifEntity> userNotifDeleteCriteria = criteriaBuilder
				.createCriteriaDelete(UserNotifEntity.class);
		CriteriaQuery<UserNotifAppliEntity> userNotifAppliCriteria = criteriaBuilder
				.createQuery(UserNotifAppliEntity.class);
		Root<UserNotifAppliEntity> userNotifAppliRoot = userNotifAppliCriteria.from(UserNotifAppliEntity.class);
		userNotifAppliCriteria.select(userNotifAppliRoot);
		List<UserNotifAppliEntity> userNotifAppliEntities = entityManager
				.createQuery(userNotifAppliCriteria).getResultList();
		Root<UserNotifEntity> userNotifRoot = userNotifCriteria.from(UserNotifEntity.class);
		Root<UserNotifEntity> userNotifDeleteRoot = userNotifDeleteCriteria.from(UserNotifEntity.class);

		List<UserNotifEntity> userNotifEntities = entityManager.createQuery(userNotifCriteria.where(
				criteriaBuilder.equal(userNotifRoot.get("user"), user))).getResultList();
		Map<TypeNotificationEntity, List<UserNotifAppliEntity>> userNotifMap = new HashMap<>();
		for (UserNotifEntity userNotif : userNotifEntities) {
			userNotifMap.put(userNotif.getTypeNotif(), null);
		}

		for (Entry<TypeNotificationEntity, List<UserNotifAppliEntity>> typeNotification : userNotifMap.entrySet()) {

			entityManager.createQuery(userNotifDeleteCriteria.where(criteriaBuilder.and(
					criteriaBuilder.equal(userNotifDeleteRoot.get("user"), user),
					criteriaBuilder.equal(userNotifDeleteRoot.get("typeNotif"), typeNotification.getKey()))))
					.executeUpdate();

		}
		entityManager.getTransaction().commit();

		log.error("@@@ End");
	}

}
