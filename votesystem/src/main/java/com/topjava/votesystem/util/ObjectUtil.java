package com.topjava.votesystem.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class ObjectUtil {
    public static Long getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Long.parseLong(paramId);
    }
}
