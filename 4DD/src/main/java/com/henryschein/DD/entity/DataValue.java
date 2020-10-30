package com.henryschein.DD.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "VALUE")
public class DataValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String equation;
    private String value;

    public boolean doEvaluateEquation() {
        if (this.equation.isEmpty() || !this.equation.startsWith("=")) {
            return false;
        }
        return true;
    }
}
