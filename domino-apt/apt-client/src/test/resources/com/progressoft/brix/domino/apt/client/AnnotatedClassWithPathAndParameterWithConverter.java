package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.annotations.PathParameter;
import com.progressoft.brix.domino.api.client.request.ClientRequest;

import java.math.BigDecimal;

@Path(path = "somePath")
public class AnnotatedClassWithPathAndParameterWithConverter extends ClientRequest<PresenterInterface> {

    private BigDecimal value;

    public AnnotatedClassWithPathAndParameterWithConverter(@PathParameter(converter = BigDecimalConverter.class) BigDecimal value) {
        this.value = value;
    }

    @Override
    protected void process(PresenterInterface presenter) {

    }
}