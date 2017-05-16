package com.progressoft.brix.domino.apt.server.multiMix;

import com.progressoft.brix.domino.api.server.Interceptor;
import com.progressoft.brix.domino.api.server.RequestInterceptor;

@Interceptor
public class SecondInterceptor implements RequestInterceptor<SecondRequest, TestServerEntryPointContext>{

    @Override
    public void intercept(SecondRequest request, TestServerEntryPointContext entryPoint) {

    }
}

