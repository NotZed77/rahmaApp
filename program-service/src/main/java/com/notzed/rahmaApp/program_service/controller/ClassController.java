package com.notzed.rahmaApp.program_service.controller;

import com.notzed.rahmaApp.program_service.dto.ClassDto;
import com.notzed.rahmaApp.program_service.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/class")
public class ClassController {

    private final ClassService classService;

    @PostMapping
    public ResponseEntity<ClassDto> createNewClass(@RequestBody ClassDto classDto) {
        return new ResponseEntity<>(classService.createClass(classDto), HttpStatus.CREATED);
    }

    @PutMapping("/{classId}")
    public ResponseEntity<ClassDto> updateClassById(@PathVariable Long classId, @RequestBody ClassDto classDto) {
        return new ResponseEntity<>(classService.updateClassById(classId, classDto), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{classId}")
    public ResponseEntity<ClassDto> getClassById(@PathVariable Long classId) {
        return new ResponseEntity<>(classService.getClassById(classId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ClassDto>> getAllClasses() {
        return new ResponseEntity<>(classService.getAllClasses(), HttpStatus.OK);
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<Void> deleteClassById(@PathVariable Long classId) {
        classService.deleteClassById(classId);
        return ResponseEntity.noContent().build();
    }

}
