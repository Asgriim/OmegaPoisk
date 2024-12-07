package org.omega.reviewservice.utils;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;


@NoArgsConstructor
public class HeaderUtils {

    public static HttpHeaders createPageHeaders(Pageable pageable, long totalCount) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Page-Size", String.valueOf(pageable.getPageSize()));
        httpHeaders.add("Page-Number", String.valueOf(pageable.getPageNumber()));
        httpHeaders.add("Total-Count", String.valueOf(totalCount));
        return httpHeaders;
    }
}
