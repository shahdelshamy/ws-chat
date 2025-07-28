package com.example.wschat.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatuses {

    ONLINE(1,"Online"),
    OFFLINE(2,"Offline");

    private int id;
    private String status;



}
