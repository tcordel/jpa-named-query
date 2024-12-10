package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="TYPENOTIFICATION")
@Getter @Setter
public class TypeNotificationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pk;
}
