package com.progressoft.brix.domino.apt.server.multiInterceptors;

import com.progressoft.brix.domino.api.server.Interceptor;
import com.progressoft.brix.domino.api.server.RequestInterceptor;

@Interceptor
public class ThirdInterceptor implements RequestInterceptor<ThirdRequest, TestServerEntryPointContext>{

    @Override
    public void intercept(ThirdRequest request, TestServerEntryPointContext entryPoint) {

    }
}

