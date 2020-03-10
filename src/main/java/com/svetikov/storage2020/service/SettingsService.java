package com.svetikov.storage2020.service;

import com.svetikov.storage2020.models.SettingsST;
import com.svetikov.storage2020.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Qualifier("settingsST")
@Service
public class SettingsService implements ModelService<SettingsST, Integer> {
    private final SettingsRepository settingsRepository;

    @Autowired
    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }


    @Override
    public SettingsST saveModel(SettingsST settings) {
        settingsRepository.save(settings);
        return settings;
    }

    @Override
    public SettingsST getModelByID(Integer integer) {
        settingsRepository.findById(integer);
        return settingsRepository.findById(integer).get();
    }

    @Override
    public List<SettingsST> getAllModel() {
        settingsRepository.findAll();
        return settingsRepository.findAll();
    }

    @Override
    public SettingsST deleteModel(Integer integer) {
        settingsRepository.deleteById(integer);
        return null;
    }

    public void findAndDeleteLastSettings() {

        settingsRepository.deleteAll();
    }
}
