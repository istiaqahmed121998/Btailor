package com.backend.userauthserivce.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.ObjectPostProcessor;

public class RateLimitedAuthenticationProviderProcessor<T extends AuthenticationProvider> implements ObjectPostProcessor<T> {
    private final Class<T> clazz;

    public RateLimitedAuthenticationProviderProcessor(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public <O extends T> O postProcess(O object) {
        if (clazz.isAssignableFrom(object.getClass())) {
            return (O) new RateLimitedAuthenticationProvider(object);
        }
        return object;
    }
}
