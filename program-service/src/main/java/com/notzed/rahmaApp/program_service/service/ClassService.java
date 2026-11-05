package com.notzed.rahmaApp.program_service.service;

import com.notzed.rahmaApp.program_service.dto.ClassDto;
import com.notzed.rahmaApp.program_service.entity.Class;
import com.notzed.rahmaApp.program_service.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;
    private final ModelMapper modelMapper;

    public ClassDto getClassById(Long classId) {
        Class aClass = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with ID: {}"+ classId));
        return modelMapper.map(aClass, ClassDto.class);
    }

    public List<ClassDto> getAllClasses() {
        List<Class> aClass = classRepository.findAll();
        return Collections.singletonList(modelMapper.map(aClass, ClassDto.class));
    }

    public ClassDto createClass(ClassDto classDto) {
        Class aClass = modelMapper.map(classDto, Class.class);
        aClass = classRepository.save(aClass);
        return modelMapper.map(aClass, ClassDto.class);
    }

    public ClassDto updateClassById(Long classId, ClassDto classDto) {
        Class aClass = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with ID: {}"+ classId));
        if(aClass.getCapacity() == null) aClass.setCapacity(classDto.getCapacity());
        else if(aClass.getInstructor() == null) aClass.setInstructor(classDto.getInstructor());
        else if(aClass.getProgram() == null) aClass.setProgram(classDto.getProgram());
        else if(aClass.getSchedule() == null) aClass.setSchedule(classDto.getSchedule());
        return modelMapper.map(aClass, ClassDto.class);
    }

    public void deleteClassById(Long classId){
        boolean aClass = classRepository.existsById(classId);
        if(aClass){
            classRepository.deleteById(classId);
        }else{
            throw new RuntimeException("Class couldn't be deleted with ID: {}"+ classId);
        }
    }


}
