package com.example.doan.autoseed;

import com.example.doan.entities.Faculties;
import com.example.doan.entities.MajorEntity;
import com.example.doan.repository.FacultyRepository;
import com.example.doan.repository.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class autoseed implements CommandLineRunner {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Override
    public void run(String... args) throws Exception {
            List<Faculties> facultiesList=facultyRepository.findAll();
            if(facultiesList.isEmpty()){
                Faculties dpt =Faculties.builder()
                        .name("Đa phương tiện")
                        .build();
                MajorEntity cndpt =MajorEntity.builder()
                        .faculties(dpt)
                        .name("Công nghệ đa phương tiện")
                        .build();
                dpt.setMajors(Collections.singletonList(cndpt));
                facultyRepository.save(dpt);
                Faculties cntt =Faculties.builder()
                        .name("Công nghệ thông tin")
                        .build();
                MajorEntity cnttm =MajorEntity.builder()
                        .faculties(cntt)
                        .name("Công nghệ thông tin")
                        .build();
                MajorEntity at =MajorEntity.builder()
                        .faculties(cntt)
                        .name("An toàn thông tin")
                        .build();
                List<MajorEntity> list=new ArrayList<>();
                list.add(cnttm);
                list.add(at);
                cntt.setMajors(list);
                facultyRepository.save(cntt);
                list.add(at);
                majorRepository.saveAll(list);
            }

    }
}
