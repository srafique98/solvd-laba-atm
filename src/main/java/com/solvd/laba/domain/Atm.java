package com.solvd.laba.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class Atm {
    private static final Logger LOGGER = LogManager.getLogger(Atm.class);
    private Long id;
    private String city;
    private List <User> users;

}
