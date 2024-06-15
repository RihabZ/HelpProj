package com.rihab.interventions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketDurationDTO {
    private String interCode;
    private String interDesignation;
    private Date datePrevue;
    private Integer dureePrevue;
    private Long dureeRealisation;
}
