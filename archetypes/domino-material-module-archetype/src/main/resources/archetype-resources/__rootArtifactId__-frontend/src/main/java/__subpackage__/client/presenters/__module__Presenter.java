#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.presenters;

import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.api.shared.extension.MainExtensionPoint;
import com.progressoft.brix.domino.api.shared.extension.MainContext;
import com.progressoft.brix.domino.api.client.annotations.InjectContext;

public interface ${module}Presenter extends Presentable{

    @InjectContext(extensionPoint=MainExtensionPoint.class)
    void contributeToMainModule(MainContext context);
}