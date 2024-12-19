package org.omega.common.core.security;

import java.security.PublicKey;

public interface SecurityHelper {
    PublicKey getJwtValidationKey();
}

