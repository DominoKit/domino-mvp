package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.request.ClientRequest;
import com.progressoft.brix.domino.api.client.annotations.Request;
import com.progressoft.brix.domino.apt.client.ContributionPresenterInterface;
import com.progressoft.brix.domino.api.shared.extension.MainExtensionPoint;

@Request
public class ObtainMainExtensionPointForContributionPresenterInterfaceClientRequest extends ClientRequest<ContributionPresenterInterface>{

    private MainExtensionPoint extensionPoint;

    public ObtainMainExtensionPointForPresenterInterfaceClientRequest(MainExtensionPoint extensionPoint){

        this.extensionPoint=extensionPoint;
    }

    @Override
    protected void process(ContributionPresenterInterface presenter){

        presenter.onMainContextReceived(extensionPoint.context());
    }
}