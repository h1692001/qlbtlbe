package com.example.doan.dtos;

import com.example.doan.entities.LogEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {
    private Date startDate;
    private Date endDate;
    private LogEnum type;
    private Long id;
    private int teachers;
    private int students;
}
