package com.honda.interauto.dto;

import lombok.Data;

@Data
public class CaseResOverViewDto {

    private Integer id;
    private Integer proId;
    private String tagId;
    private Integer totalCount;
    private Integer failCount;
    private Integer successCount;
    private String operator;
    private String startTime;
    private String endTime;
    private String proName;
    private String proDesc;
}
