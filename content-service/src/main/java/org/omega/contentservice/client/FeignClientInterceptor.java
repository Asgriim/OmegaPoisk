package org.omega.contentservice.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.omega.contentservice.service.TokenService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {
    private final TokenService tokenService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        System.out.println("ia interceptor ");
        String token = tokenService.getToken();
        if (token != null) {
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }

}
