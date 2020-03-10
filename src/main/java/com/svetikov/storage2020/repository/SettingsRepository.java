package com.svetikov.storage2020.repository;

import com.svetikov.storage2020.models.SettingsST;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<SettingsST,Integer> {
}
