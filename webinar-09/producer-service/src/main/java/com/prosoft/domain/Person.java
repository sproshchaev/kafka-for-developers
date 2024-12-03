package com.prosoft.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Person implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private int age;
}
