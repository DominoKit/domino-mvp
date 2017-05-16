package com.progressoft.brix.domino.apt.server.multiMix;

import com.progressoft.brix.domino.api.server.Interceptor;
import com.progressoft.brix.domino.api.server.RequestInterceptor;

@Interceptor
public class FirstInterceptor implements RequestInterceptor<FirstRequest, TestServerEntryPointContext>{

    @Override
    public void intercept(FirstRequest request, TestServerEntryPointContext entryPoint) {

    }
}

