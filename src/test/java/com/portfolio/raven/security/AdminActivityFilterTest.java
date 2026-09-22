package com.portfolio.raven.security;

import com.portfolio.raven.service.AdminControlService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

class AdminActivityFilterTest {
    @Test void recordsRequestMetadataWithoutSecrets() throws Exception {
        var controls=mock(AdminControlService.class);
        var request=new MockHttpServletRequest("POST","/login");
        request.setQueryString("password=do-not-log");
        request.addHeader("Authorization","Bearer do-not-log");
        request.setContent("password=do-not-log".getBytes());
        var response=new MockHttpServletResponse();
        new AdminActivityFilter(controls).doFilter(request,response,(req,res)->((jakarta.servlet.http.HttpServletResponse)res).setStatus(401));
        verify(controls).record(isNull(),eq("POST"),eq("/login"),eq(401),anyLong());
        verifyNoMoreInteractions(controls);
    }
}
