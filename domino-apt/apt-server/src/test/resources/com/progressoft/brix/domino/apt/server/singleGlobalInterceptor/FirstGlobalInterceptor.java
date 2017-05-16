package com.progressoft.brix.domino.apt.server.singleGlobalInterceptor;

import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.server.GlobalInterceptor;
import com.progressoft.brix.domino.api.server.GlobalRequestInterceptor;

@GlobalInterceptor
public class FirstGlobalInterceptor implements GlobalRequestInterceptor<TestServerEntryPointContext>{

    @Override
    public void intercept(ServerRequest request, TestServerEntryPointContext context) {

    }
}

