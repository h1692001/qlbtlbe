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
        if(facultyRepository.count()==0&&majorRepository.count()==0){
            List<String> listCT = new ArrayList<>();
            listCT.add("Xây dựng Cầu đường bộ");
            listCT.add("Quy hoạch và xây dựng giao thông");
            listCT.add("Quản lý dự án");
            listCT.add("Quản lý chất lượng công trình và xây dựng");
            listCT.add("Xây dựng Đường sắt - Metro");
            listCT.add("Xây dựng Cảng - Đường thủy và Công trình biển");
            listCT.add("Xây dựng dân dụng và công nghiệp");
            listCT.add("Công nghệ kỹ thuật môi trường");
            create("Công trình", listCT);
            List<String> listCT2 = new ArrayList<>();
            listCT2.add("Kỹ thuật ô tô");
            listCT2.add("Cơ điện tử trên ô tô");
            listCT2.add("Cơ khí máy xây dựng");
            listCT2.add("Cơ khí chế tạo");
            listCT2.add("Tàu thủy và thiết bị nỗi");
            listCT2.add("Đầu máy - Toa xe và tàu điện Metro ");
            create("Cơ khí", listCT2);
            List<String> listCT3 = new ArrayList<>();
            listCT3.add("Logistics và quản lý chuỗi cung ứng");
            listCT3.add("Thương mại điện tử");
            listCT3.add("Kế toán doanh nghiệp");
            listCT3.add("Hệ thống thông tin Kế toán tài chính ");
            listCT3.add("Kinh tế xây dựng");
            listCT3.add("Quản trị doanh nghiệp");
            listCT3.add("Quản trị marketing");
            listCT3.add("Quản trị Tài chính và Đầu tư");
            listCT3.add("Tài chính - Ngân hàng");
            listCT3.add("Logistics và Vận tải đa phương thức");
            create("Kinh tế", listCT3);
            List<String> listCT4 = new ArrayList<>();
            listCT4.add("Cơ điện tử");
            listCT4.add("Công nghệ thông tin");
            listCT4.add("Hệ thống thông tin ");
            listCT4.add("Mạng máy tính và truyền thông dữ liệu");
            listCT4.add("Điện tử - Viễn thông");
            create("Công nghệ thông tin", listCT4);
        }

    }

    public void create(String faculty, List<String> major) {
        Faculties dpt = Faculties.builder().name(faculty).build();

        List<MajorEntity> majorEntities = new ArrayList<>();
        major.forEach(mj -> {
            MajorEntity majorEntity = MajorEntity.builder().name(mj).faculties(dpt).build();
            majorEntities.add(majorEntity);
        });

        dpt.setMajors(majorEntities);
        facultyRepository.save(dpt);
        majorRepository.saveAll(majorEntities);
    }


}
