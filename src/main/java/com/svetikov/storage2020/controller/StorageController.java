package com.svetikov.storage2020.controller;

import com.svetikov.storage2020.models.BoardBox;
import com.svetikov.storage2020.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/app/storage/board")
public class StorageController {

    private final ModelService<BoardBox, Long> modelService;

    @Autowired
    public StorageController(@Qualifier("board") ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping("/{x}/{z}")
    public ResponseEntity<List<BoardBox>> addBoard(@PathVariable("x") int x, @PathVariable("z") int z) {
        modelService.saveModel(new BoardBox(x, z, 1250, 1600, 16));
        return new ResponseEntity<>(modelService.getAllModel(), HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<List<BoardBox>> deleteBoard(@PathVariable("id") long id) {

        modelService.deleteModel(id);
        return new ResponseEntity<>(modelService.getAllModel(), HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<BoardBox>> showAllBoards(){
        return new ResponseEntity<>(modelService.getAllModel(),HttpStatus.OK);
    }
}
