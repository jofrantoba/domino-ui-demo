package org.dominokit.domino.ui.utils;

import elemental2.dom.ClipboardEvent;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.KeyboardEvent;
import jsinterop.base.Js;
import org.dominokit.domino.ui.forms.TextBox;
import org.jboss.gwt.elemento.core.Elements;
import org.jboss.gwt.elemento.core.ObserverCallback;
import org.jboss.gwt.elemento.core.builder.HtmlContentBuilder;

import static java.util.Objects.nonNull;

public class ElementUtil {

    public static void clear(Element element) {
        if (nonNull(element))
            while (nonNull(element.firstChild))
                element.removeChild(element.firstChild);
    }

    public static <T extends HTMLElement> HtmlContentBuilder<T> builderFor(T element) {
        return new HtmlContentBuilder<>(element);
    }

    public static boolean isKeyOf(String keyCode, KeyboardEvent keyboardEvent) {
        return keyCode.equalsIgnoreCase(keyboardEvent.code);
    }

    public static boolean isEnterKey(KeyboardEvent keyboardEvent) {
        return isKeyOf("enter", keyboardEvent);
    }

    public static boolean isSpaceKey(KeyboardEvent keyboardEvent) {
        return isKeyOf("space", keyboardEvent);
    }

    /**
     * Registers a callback when an element is appended to the document body. Note that the callback will be called
     * only once, if the element is appended more than once a new callback should be registered.
     *
     * @param element  the HTML element which is going to be added to the body
     * @param callback {@link ObserverCallback}
     */
    public static void onAttach(HTMLElement element, ObserverCallback callback) {
        Elements.onAttach(element, callback);
//        if (element != null) {
//            BodyObserver.addAttachObserver(element, callback);
//        }
    }

    /**
     * Registers a callback when an element is removed from the document body. Note that the callback will be called
     * only once, if the element is removed and re-appended a new callback should be registered.
     *
     * @param element  the HTML element which is going to be removed from the body
     * @param callback {@link ObserverCallback}
     */
    public static void onDetach(HTMLElement element, ObserverCallback callback) {

        Elements.onDetach(element, callback);
//        if (element != null) {
//            BodyObserver.addDetachObserver(element, callback);
//        }
    }

    public static TextBox numbersOnly(TextBox textBox) {
        textBox.getInputElement().addEventListener("keypress", evt -> {
            KeyboardEvent keyboardEvent = Js.uncheckedCast(evt);
            if (!keyboardEvent.key.matches("^\\d+$")) {
                evt.preventDefault();
            }
        });
        textBox.getInputElement().addEventListener("paste", evt -> {
            ClipboardEvent clipboardEvent = Js.uncheckedCast(evt);
            if (!clipboardEvent.clipboardData.getData("text").matches("^\\d+$")) {
                evt.preventDefault();
            }
        });
        return textBox;
    }
}
