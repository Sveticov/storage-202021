package com.svetikov.storage2020.controller;

import com.svetikov.storage2020.models.SettingsST;
import com.svetikov.storage2020.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
@RestController
@RequestMapping("/app/settings")
public class SettingsController {

    private final SettingsService settingsService;

    @Autowired
    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @PostMapping("/add")

    public ResponseEntity<List<SettingsST>> addSettings(@RequestBody SettingsST settings) {
        log.info("add settings");
        log.info(settings.toString());
        if (!settingsService.getAllModel().isEmpty()) {
            log.error("empty list");
            settingsService.findAndDeleteLastSettings();
        }

        settingsService.saveModel(settings);

        return new ResponseEntity<>(settingsService.getAllModel(), HttpStatus.OK);
    }

    @GetMapping("/last")
    public ResponseEntity<List<SettingsST>> lastSettings() {

        return new ResponseEntity<>(settingsService.getAllModel(), HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<List<SettingsST>> deleteSettings(@PathVariable("id") int id) {
        settingsService.deleteModel(id);
        return new ResponseEntity<>(settingsService.getAllModel(), HttpStatus.OK);
    }

    @GetMapping("/last/{id}")
    public ResponseEntity<SettingsST> lastID(@PathVariable("id") int id) {
        return new ResponseEntity<>(settingsService.getModelByID(id), HttpStatus.OK);
    }

}
