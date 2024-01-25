package com.solvd.laba.persistence.interfaces;

import com.solvd.laba.domain.User;

public interface UserRepository {
    void create(User user, Long atmID, Long credentialID);

    User findById(Long id);

    void updateById(User user);
}
