package org.omega.omegapoisk.utils;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class HeaderUtils {

    public HttpHeaders createPageHeaders(int pageNumber, int pageSize, long totalCount) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Page-Size", String.valueOf(pageSize));
        httpHeaders.add("Page-Number", String.valueOf(pageNumber));
        httpHeaders.add("Total-Count", String.valueOf(totalCount));
        return httpHeaders;
    }
}
