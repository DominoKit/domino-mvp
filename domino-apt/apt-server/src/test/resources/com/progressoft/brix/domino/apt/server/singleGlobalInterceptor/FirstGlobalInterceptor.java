package com.progressoft.brix.domino.apt.server.singleGlobalInterceptor;

import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.server.interceptor.GlobalInterceptor;
import com.progressoft.brix.domino.api.server.interceptor.GlobalRequestInterceptor;

@GlobalInterceptor
public class FirstGlobalInterceptor implements GlobalRequestInterceptor<TestServerEntryPointContext>{

    @Override
    public void intercept(RequestBean request, TestServerEntryPointContext context) {
        //for generation testing only
    }
}

