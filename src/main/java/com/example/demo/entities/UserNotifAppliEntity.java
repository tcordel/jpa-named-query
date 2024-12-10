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
import lombok.Setter;

@Entity
@Table(name = "USER_NOTIF_APPLI")
@Getter @Setter
public class UserNotifAppliEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pk;

	@ManyToOne
	@JoinColumn(name = "PK_USER_NOTIF", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserNotifEntity userNotif;

	@ManyToOne
	@JoinColumn(name = "PK_APPLICATION", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private ApplicationEntity application;
	
}
