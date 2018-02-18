package com.progressoft.brix.domino.ui.button;

import com.progressoft.brix.domino.ui.icons.Icon;
import com.progressoft.brix.domino.ui.style.WaveStyle;
import org.jboss.gwt.elemento.core.Elements;

import static java.util.Objects.nonNull;

public class IconButton extends Button {

    private Icon icon;
    private String content;

    private IconButton(Icon icon) {
        this(icon, ButtonType.DEFAULT);
    }

    private IconButton(Icon icon, ButtonType type) {
        setButtonType(type);
        setIcon(icon);
    }

    public static IconButton create(Icon icon) {
        return new IconButton(icon);
    }

    private static IconButton create(Icon icon, ButtonType type) {
        return new IconButton(icon, type);
    }

    public static IconButton createDefault(Icon icon) {
        return create(icon, ButtonType.DEFAULT);
    }

    public static IconButton createPrimary(Icon icon) {
        return create(icon, ButtonType.PRIMARY);
    }

    public static IconButton createSuccess(Icon icon) {
        return create(icon, ButtonType.SUCCESS);
    }

    public static IconButton createInfo(Icon icon) {
        return create(icon, ButtonType.INFO);
    }

    public static IconButton createWarning(Icon icon) {
        return create(icon, ButtonType.WARNING);
    }

    public static IconButton createDanger(Icon icon) {
        return create(icon, ButtonType.DANGER);
    }

    public IconButton setIcon(Icon icon) {
        this.icon = icon;
        if (nonNull(content)) {
            buttonElement.textContent = "";
            buttonElement.appendChild(icon.asElement());
            buttonElement.appendChild(Elements.span().textContent(content).asElement());
        } else
            buttonElement.appendChild(icon.asElement());
        return this;
    }

    public IconButton circle(CircleSize size) {
        buttonElement.classList.add(size.style);
        applyCircleWaves();
        return this;
    }

    private void applyCircleWaves() {
        applyWaveStyle(WaveStyle.CIRCLE);
        applyWaveStyle(WaveStyle.FLOAT);
    }

    @Override
    public IconButton setContent(String content) {
        this.content = content;
        buttonElement.textContent = "";
        buttonElement.appendChild(icon.asElement());
        buttonElement.appendChild(Elements.span().textContent(content).asElement());
        return this;
    }

    public enum CircleSize {
        SMALL("btn-circle"),
        LARGE("btn-circle-lg");

        private String style;

        CircleSize(String style) {
            this.style = style;
        }
    }
}
