package org.omega.common.security;

import java.security.PublicKey;

public interface SecurityHelper {
    PublicKey getJwtValidationKey();
}

