package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.GenerateCommand;
import org.dominokit.domino.api.client.annotations.presenter.Presenter;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;

@Presenter
@GenerateCommand
public class SomePresenter implements Presentable {
}