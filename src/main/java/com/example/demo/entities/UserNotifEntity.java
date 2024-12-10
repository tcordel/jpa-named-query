package com.example.demo.entities;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="USERNOTIF")
@NoArgsConstructor
@Getter @Setter
public class UserNotifEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pk;

	@ManyToOne
	@JoinColumn(name="PK_USER", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UtilisateurEntity user;

	@ManyToOne
	@JoinColumn(name="PK_TYPENOTIFICATION", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private TypeNotificationEntity typeNotif;

	public UserNotifEntity(UtilisateurEntity user, TypeNotificationEntity typeNotif) {
		this.user = user;
		this.typeNotif = typeNotif;
	}
	
}
