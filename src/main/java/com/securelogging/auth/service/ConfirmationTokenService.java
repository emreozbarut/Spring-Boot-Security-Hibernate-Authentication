package com.securelogging.auth.service;

import com.securelogging.auth.model.ConfirmationToken;

public interface ConfirmationTokenService {
    void save(ConfirmationToken confirmationToken);

    ConfirmationToken findByToken(String token);
}
