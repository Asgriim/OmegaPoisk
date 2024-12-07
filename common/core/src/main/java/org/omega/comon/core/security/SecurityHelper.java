package org.omega.comon.core.security;

import java.security.PublicKey;

public interface SecurityHelper {
    PublicKey getJwtValidationKey();
}

