package com.progressoft.brix.domino.apt.server.multiGlobalInterceptors;

import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.server.interceptor.GlobalInterceptor;
import com.progressoft.brix.domino.api.server.interceptor.GlobalRequestInterceptor;

@GlobalInterceptor
public class ThirdGlobalInterceptor implements GlobalRequestInterceptor<TestServerEntryPointContext>{

    @Override
    public void intercept(RequestBean request, TestServerEntryPointContext context) {
        //for code generation testing only
    }
}

