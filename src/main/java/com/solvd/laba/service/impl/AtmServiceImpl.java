package com.solvd.laba.service.impl;

import com.solvd.laba.domain.Atm;
import com.solvd.laba.persistence.impl.AtmDAO;
import com.solvd.laba.persistence.interfaces.AtmRepository;
import com.solvd.laba.service.interfaces.AtmService;
import com.solvd.laba.service.interfaces.UserService;

import java.util.ArrayList;

public class AtmServiceImpl implements AtmService {
    private final AtmRepository atmRepository;
    private final UserService userService;

    public AtmServiceImpl() {
        this.atmRepository = new AtmDAO();
        this.userService = new UserServiceImpl();
    }

    @Override
    public void create(Atm atm, Long credentialID) {
        atmRepository.create(atm);

        if (atm.getUsers() == null) {
            atm.setUsers(new ArrayList<>());
        }

        if (!atm.getUsers().isEmpty()) {
            atm.getUsers().forEach(user -> {
                userService.create(user,atm.getId(),credentialID);
            });
        }

    }

    @Override
    public Atm findById(Long id) {
        return atmRepository.findById(id);
    }

    @Override
    public void updateById(Atm atm) {
        atmRepository.updateById(atm);

    }
}
