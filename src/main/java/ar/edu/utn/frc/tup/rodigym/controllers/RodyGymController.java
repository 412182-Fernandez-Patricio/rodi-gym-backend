package ar.edu.utn.frc.tup.rodigym.controllers;

import ar.edu.utn.frc.tup.rodigym.dtos.DummyDto;
import ar.edu.utn.frc.tup.rodigym.models.Dummy;
import ar.edu.utn.frc.tup.rodigym.services.DummyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rodygym")
public class RodyGymController {

    private DummyService dummyService;

    public RodyGymController(DummyService dummyService) {
        this.dummyService = dummyService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping(){
        return ResponseEntity.ok("PONG");
    }

    public ResponseEntity<DummyDto> getDummyList(){
        List<Dummy> dummyList = dummyService.getDummyList();
        return null;
    }

    public ResponseEntity<DummyDto> getDummyList(@PathVariable Long id){
        Dummy dummy = dummyService.getDummy(id);
        return null;
    }

    public ResponseEntity<DummyDto> postDummy(DummyDto dummyDto){
        Dummy dummy = dummyService.createDummy(null);
        return null;
    }

    public ResponseEntity<DummyDto> putDummy(DummyDto dummyDto){
        Dummy dummy = dummyService.updateDummy(null);
        return null;
    }

    public ResponseEntity<Void> deleteDummy(DummyDto dummyDto){
        dummyService.deleteDummy(null);
        return null;
    }
}

