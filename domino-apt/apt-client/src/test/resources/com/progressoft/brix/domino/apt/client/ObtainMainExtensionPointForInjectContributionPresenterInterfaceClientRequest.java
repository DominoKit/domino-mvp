package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.request.ClientRequest;
import com.progressoft.brix.domino.api.client.annotations.Request;
import com.progressoft.brix.domino.api.shared.extension.MainExtensionPoint;

@Request
public class ObtainMainExtensionPointForInjectContributionPresenterInterfaceClientRequest extends ClientRequest<InjectContributionPresenterInterface>{

    private MainExtensionPoint extensionPoint;

    public ObtainMainExtensionPointForInjectContributionPresenterInterfaceClientRequest(MainExtensionPoint extensionPoint){

        this.extensionPoint=extensionPoint;
    }

    @Override
    protected void process(InjectContributionPresenterInterface presenter){

        presenter.onMainExtensionPointContextReceived(extensionPoint.context());
    }
}