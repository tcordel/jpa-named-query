package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.UserNotifAppliEntity;
import com.example.demo.entities.UserNotifEntity;

import java.util.List;

public interface UserNotifAppliRepository extends JpaRepository<UserNotifAppliEntity, Long> {

    List<UserNotifAppliEntity> findAllByUserNotif(UserNotifEntity userNotif);
}
