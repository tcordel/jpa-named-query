package com.example.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.TypeNotificationEntity;
import com.example.demo.entities.UserNotifAppliEntity;
import com.example.demo.entities.UtilisateurEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final NotificationService notificationService;

	public void unsubscribeWithoutTransaction(UtilisateurEntity user) {
		List<TypeNotificationEntity> typeNotifsForbidden = getTypeNotifsForbidden(user);
		for (TypeNotificationEntity typeNotif : typeNotifsForbidden) {
			notificationService.removeTypeNotif(user, typeNotif);
		}
	}

	@Transactional
	public void unsubscribeSharingTransaction(UtilisateurEntity user) {
		List<TypeNotificationEntity> typeNotifsForbidden = getTypeNotifsForbidden(user);
		for (TypeNotificationEntity typeNotif : typeNotifsForbidden) {
			notificationService.removeTypeNotif(user, typeNotif);
		}
	}

	private List<TypeNotificationEntity> getTypeNotifsForbidden(UtilisateurEntity user) {
		List<TypeNotificationEntity> typeNotifsForbidden = new ArrayList<>();
		for (Entry<TypeNotificationEntity, List<UserNotifAppliEntity>> entry : notificationService
				.makeAppNotifMapFromUserNotif(user).entrySet()) {
			typeNotifsForbidden.add(entry.getKey());
		}
		return typeNotifsForbidden;
	}

	@Transactional
	public void unsubscribeWithDeleteOnDedicatedTransatction(UtilisateurEntity user) {
		List<TypeNotificationEntity> typeNotifsForbidden = getTypeNotifsForbidden(user);
		for (TypeNotificationEntity typeNotif : typeNotifsForbidden) {
			notificationService.removeTypeNotifWithNewTransaction(user, typeNotif);
		}
	}
}
