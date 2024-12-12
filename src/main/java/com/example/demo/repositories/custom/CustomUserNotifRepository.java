package com.example.demo.repositories.custom;

import com.example.demo.entities.TypeNotificationEntity;
import com.example.demo.entities.UtilisateurEntity;

public interface CustomUserNotifRepository {
	void criteriaDelete(UtilisateurEntity user, TypeNotificationEntity typeNofif);
}
