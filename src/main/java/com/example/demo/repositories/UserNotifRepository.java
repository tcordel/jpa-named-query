package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.TypeNotificationEntity;
import com.example.demo.entities.UserNotifEntity;
import com.example.demo.entities.UtilisateurEntity;

import java.util.Collection;

public interface UserNotifRepository extends JpaRepository<UserNotifEntity, Long> {
	Collection<UserNotifEntity> findAllByUser(UtilisateurEntity user);

    void deleteAllByUserAndTypeNotif(UtilisateurEntity user, TypeNotificationEntity typeNotif);
}
