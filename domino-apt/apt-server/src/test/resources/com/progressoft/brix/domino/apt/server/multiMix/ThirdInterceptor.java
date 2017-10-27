package com.progressoft.brix.domino.apt.server.multiMix;

import com.progressoft.brix.domino.api.server.interceptor.Interceptor;
import com.progressoft.brix.domino.api.server.interceptor.RequestInterceptor;

@Interceptor
public class ThirdInterceptor implements RequestInterceptor<ThirdRequest, TestServerEntryPointContext>{

    @Override
    public void intercept(ThirdRequest request, TestServerEntryPointContext entryPoint) {
        //for generation testing only
    }
}

