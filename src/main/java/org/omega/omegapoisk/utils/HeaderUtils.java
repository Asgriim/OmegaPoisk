package org.omega.omegapoisk.utils;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class HeaderUtils {

    public HttpHeaders createPageHeaders(Pageable pageable, long totalCount) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Page-Count", String.valueOf(pageable.getPageSize()));
        httpHeaders.add("Page-Number", String.valueOf(pageable.getPageNumber()));
        httpHeaders.add("Total-Count", String.valueOf(totalCount));
        return httpHeaders;
    }
}
