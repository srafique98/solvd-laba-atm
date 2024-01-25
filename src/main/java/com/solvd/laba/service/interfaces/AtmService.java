package com.solvd.laba.service.interfaces;

import com.solvd.laba.domain.Atm;

public interface AtmService {
    void create(Atm atm, Long credentialID);

    Atm findById(Long id);

    void updateById(Atm atm);
}
