package com.henryschein.DD.dto;

import com.henryschein.DD.entity.DataElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class DataValueDTO {
    private DataElement dataElement;
    private String equation;
    private String value;
}
