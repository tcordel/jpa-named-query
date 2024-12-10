package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import com.example.demo.repositories.TypeNotificationRepository;
import com.example.demo.repositories.UserNotifRepository;
import com.example.demo.repositories.UtilisateurRepository;
import com.example.demo.services.ApplicationService;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
class DemoApplicationTests {
	@Autowired
	UserService service;
	@Autowired
	NotificationService notificationService;
	@Autowired
	UserNotifRepository userNotifRepository;
	@Autowired
	UtilisateurRepository utilisateurRepository;
	@Autowired
	TypeNotificationRepository typeNotificationRepository;
	@Autowired
	ApplicationService applicationService;
	UtilisateurEntity user;

	@BeforeEach
	void init() {
		user = utilisateurRepository.save(new UtilisateurEntity());
		TypeNotificationEntity typeNotif = typeNotificationRepository.save(new TypeNotificationEntity());
		ApplicationEntity application = applicationService.save(new ApplicationEntity());
		UserNotifEntity userNotif = userNotifRepository.save(new UserNotifEntity(user, typeNotif));
		notificationService.addUserNotifAppli(userNotif, application);
	}

	@Test
	void buggyPropagatingTransaction() {
		service.unsubscribeSharingTransaction(user);
	}

	@Test
	void workingWithoutTransaction() {
		service.unsubscribeWithoutTransaction(user);
	}

	@Test
	@Disabled
	void workingWithoutTransactionPropagation() {
		List<TypeNotificationEntity> typeNotifsForbidden = new ArrayList<>();
		for (Entry<TypeNotificationEntity, List<UserNotifAppliEntity>> entry : notificationService.makeAppNotifMapFromUserNotif(user).entrySet()) {
				typeNotifsForbidden.add(entry.getKey());
		}
		for (TypeNotificationEntity typeNotif : typeNotifsForbidden) {
			notificationService.removeTypeNotif(user, typeNotif);
		}
	}

	@Test
	@Disabled
	void workingRequiringNewTransactionForDeletion() {
		service.unsubscribeWithDeleteOnDedicatedTransatction(user);
	}

	@Test
	@Disabled
	void workingUsingEntityManager () {
		notificationService.removeNotification(user);
	}
}
