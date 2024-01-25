package com.solvd.laba.persistence.interfaces;


import com.solvd.laba.domain.Atm;

public interface AtmRepository {
    void create(Atm atm);

    Atm findById(Long id);

    void updateById(Atm atm);
}
