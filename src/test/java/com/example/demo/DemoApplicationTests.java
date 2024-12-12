package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import com.example.demo.entities.ApplicationEntity;
import com.example.demo.entities.TypeNotificationEntity;
import com.example.demo.entities.UserNotifAppliEntity;
import com.example.demo.entities.UserNotifEntity;
import com.example.demo.entities.UtilisateurEntity;
import com.example.demo.repositories.ApplicationRepository;
import com.example.demo.repositories.TypeNotificationRepository;
import com.example.demo.repositories.UserNotifAppliRepository;
import com.example.demo.repositories.UserNotifRepository;
import com.example.demo.repositories.UtilisateurRepository;
import com.example.demo.services.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@RequiredArgsConstructor
@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
@Slf4j
class DemoApplicationTests {
	@Autowired
	NotificationService notificationService;
	@Autowired
	UserNotifRepository userNotifRepository;
	@Autowired
	UserNotifAppliRepository userNotifAppliRepository;
	@Autowired
	UtilisateurRepository utilisateurRepository;
	@Autowired
	TypeNotificationRepository typeNotificationRepository;
	@Autowired
	ApplicationRepository applicationRepository;

	UtilisateurEntity user;

	@BeforeEach
	void init() {
		userNotifRepository.deleteAll();
		user = utilisateurRepository.save(new UtilisateurEntity());
		TypeNotificationEntity typeNotif = typeNotificationRepository.save(new TypeNotificationEntity());
		ApplicationEntity application = applicationRepository.save(new ApplicationEntity());
		UserNotifEntity userNotif = userNotifRepository.save(new UserNotifEntity(user, typeNotif));
		userNotifAppliRepository.save(new UserNotifAppliEntity(userNotif, application));
	}


	@Test
	void buggyWithTransactionalUsingNamedQueryForDeletion() {
		log.error("@@@ Start");
		assertThatNoException()
			.isThrownBy(() -> notificationService.unsubscribeWithTransactional(user));
		assertThat(userNotifRepository.findAll())
			.isEmpty();
	}


	@Test
	void workingInSameConditionWithJpqlForDeletion() {
		log.error("@@@ Start");
		notificationService.unsubscribeWithTransactionalUsingJpqlForDeletion(user);
		assertThat(userNotifRepository.findAll())
			.isEmpty();
	}

	@Test
	void workingInSameConditionWithCriteriaForDeletion() {
		log.error("@@@ Start");
		notificationService.unsubscribeWithTransactionalUsingCriteriaForDeletion(user);
		assertThat(userNotifRepository.findAll())
			.isEmpty();
	}
	@Test
	void workingWithoutTransactional() {
		notificationService.unsubscribeWithoutTransactional(user);
		assertThat(userNotifRepository.findAll())
			.isEmpty();
	}

	@Test
	void workingRequiringNewTransactionForDeletion() {
		notificationService.unsubscribeWithTransactionRequiresNewOnDelete(user);
		assertThat(userNotifRepository.findAll())
			.isEmpty();
	}

	@Test
	void workingUsingEntityManager () {
		notificationService.removeNotificationUsingEntityManagerFactory(user);
		assertThat(userNotifRepository.findAll())
			.isEmpty();
	}
}
