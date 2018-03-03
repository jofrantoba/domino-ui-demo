package com.progressoft.brix.domino.profile.client.views.ui;

import com.google.gwt.core.client.GWT;
import com.progressoft.brix.domino.api.client.annotations.UiView;
import com.progressoft.brix.domino.layout.shared.extension.IsLayout;
import com.progressoft.brix.domino.profile.client.presenters.ProfilePresenter;
import com.progressoft.brix.domino.profile.client.views.ProfileView;
import com.progressoft.brix.domino.ui.cards.Card;
import com.progressoft.brix.domino.ui.icons.Icon;
import com.progressoft.brix.domino.ui.icons.Icons;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLIElement;
import jsinterop.base.Js;
import org.jboss.gwt.elemento.core.Elements;


@UiView(presentable = ProfilePresenter.class)
public class ProfileViewImpl implements ProfileView{

    private final Card profile=Card.createProfile("Vegegoku", "vegegoku@bo3.com");

    public ProfileViewImpl() {
    }

    @Override
    public void setLayout(IsLayout layout) {
        HTMLElement leftPanel= Js.cast(layout.getLeftPanel().get());
        if(leftPanel.childElementCount>0)
            leftPanel.insertBefore(profile.asElement(), leftPanel.firstChild);
        else
            leftPanel.appendChild(profile.asElement());

        profile.getBody().appendChild(Elements.img(GWT.getModuleBaseURL()+"/images/user.png").style("border-radius:50%;").asElement());
        profile.getHeaderBar().appendChild(createIcon(Icons.ALL.more_vert()));
        profile.asElement().style.height= CSSProperties.HeightUnionType.of(300);
    }

    private HTMLLIElement createIcon(Icon icon) {
        return Elements.li().add(
                Elements.a().add(icon.asElement()))
                .asElement();
    }
}