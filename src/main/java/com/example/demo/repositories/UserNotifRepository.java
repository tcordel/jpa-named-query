package com.example.demo.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.TypeNotificationEntity;
import com.example.demo.entities.UserNotifEntity;
import com.example.demo.entities.UtilisateurEntity;

import java.util.Collection;

public interface UserNotifRepository extends CrudRepository<UserNotifEntity, Long> {
	Collection<UserNotifEntity> findAllByUser(UtilisateurEntity user);

	@Transactional
    void deleteAllByUserAndTypeNotif(UtilisateurEntity user, TypeNotificationEntity typeNotif);

	@Query("DELETE FROM UserNotifEntity une WHERE une.user=:user AND une.typeNotif=:typeNotif")
	@Modifying
	@Transactional
	void jpqlDelete(UtilisateurEntity user, TypeNotificationEntity typeNotif);
	
	@NativeQuery("DELETE FROM USERNOTIF WHERE PK_USER=:pkUser AND PK_TYPENOTIFICATION=:pkTypeNotif")
	@Modifying
	@Transactional
	void nativeDelete(long pkUser, long pkTypeNotif);
}
