package com.example.demo.repositories.custom;

import com.example.demo.entities.TypeNotificationEntity;
import com.example.demo.entities.UserEntity;

public interface CustomUserNotifRepository {
	void criteriaDelete(UserEntity user, TypeNotificationEntity typeNofif);
}
