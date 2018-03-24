package org.dominokit.domino.componentcase.client.presenters;

import org.dominokit.domino.api.client.annotations.InjectContext;
import org.dominokit.domino.api.client.annotations.Presenter;
import org.dominokit.domino.api.client.extension.ContextAggregator;
import org.dominokit.domino.api.client.mvp.presenter.BaseClientPresenter;
import org.dominokit.domino.api.shared.history.HistoryToken;
import org.dominokit.domino.api.shared.history.TokenFilter;
import org.dominokit.domino.componentcase.client.views.ComponentCaseView;
import org.dominokit.domino.componentcase.shared.extension.ComponentCase;
import org.dominokit.domino.componentcase.shared.extension.ComponentCaseContext;
import org.dominokit.domino.componentcase.shared.extension.ComponentCaseExtensionPoint;
import org.dominokit.domino.layout.shared.extension.LayoutContext;
import org.dominokit.domino.layout.shared.extension.LayoutExtensionPoint;
import org.dominokit.domino.menu.shared.extension.MenuContext;
import org.dominokit.domino.menu.shared.extension.MenuExtensionPoint;
import org.dominokit.domino.componentcase.client.views.ComponentCaseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.dominokit.domino.menu.shared.extension.MenuContext.CanAddMenuItem;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Presenter
public class ComponentCasePresenter extends BaseClientPresenter<ComponentCaseView> implements ComponentCaseContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentCasePresenter.class);

    private ContextAggregator.ContextWait<MenuContext> menuContext = ContextAggregator.ContextWait.create();
    private ContextAggregator.ContextWait<LayoutContext> layoutContext = ContextAggregator.ContextWait.create();
    private ComponentCase currentSample;


    @Override
    public void initView(ComponentCaseView view) {
        ContextAggregator.waitFor(menuContext).and(layoutContext).onReady(() -> {
            view.init(layoutContext.get().getLayout());
            applyContributions(ComponentCaseExtensionPoint.class, () -> ComponentCasePresenter.this);
            if(history().currentToken().fragment().isEmpty()) {
                HistoryToken historyToken=history().currentToken();
                historyToken.appendFragment("home");
                history().pushState(historyToken.value());
                history().forward();
            }
        });
    }

    public class MenuBranch {
        private final String title;
        private final CanAddMenuItem menuItem;
        private final Map<String, MenuBranch> leaves = new HashMap<>();

        public MenuBranch(String title, CanAddMenuItem menuItem) {
            this.title = title;
            this.menuItem = menuItem;
        }
    }

    private Map<String, MenuBranch> roots = new HashMap<>();

    @InjectContext(extensionPoint=MenuExtensionPoint.class)
    public void contributeToMenuModule(MenuContext context) {
        menuContext.receiveContext(context);
    }

    @InjectContext(extensionPoint=LayoutExtensionPoint.class)
    public void contributeToLayoutModule(LayoutContext context) {
        layoutContext.receiveContext(context);
    }

    @Override
    public void addComponentCase(ComponentCase componentCase) {

        boolean rootPage = componentCase.getMenuPath().split("/").length <= 1;
        if (componentCase.hasContent()) {
            history()
                    .listen(TokenFilter.startsWithFragment(componentCase.getHistoryToken()), state -> showPage(componentCase))
                    .onDirectUrl(state -> showPage(componentCase));
        }

        if (rootPage) {
            addRootPage(componentCase);
        } else {
            addSubPage(componentCase);
        }

    }

    private void addSubPage(ComponentCase componentCase) {
        String[] pathElements = componentCase.getMenuPath().split("/");
        if (isNull(roots.get(pathElements[0])))
            throw new NoRootMenuException("No root menu : " + pathElements[0]);

        MenuBranch root = roots.get(pathElements[0]);
        for (int index = 1; index < pathElements.length - 1; index++) {
            root = getOrAddMenuBranch(root, pathElements[index]);
        }

        addSubPageToRoot(pathElements[pathElements.length-1], componentCase, root);

    }

    private void addSubPageToRoot(String path, ComponentCase componentCase, MenuBranch root) {
        CanAddMenuItem canAddMenuItem;
        if (componentCase.hasContent()) {
            canAddMenuItem = root.menuItem.addMenuItem(path, () -> {
                applyHistory(componentCase);
                showPage(componentCase);
            });
        } else {
            canAddMenuItem = root.menuItem.addMenuItem(path);
        }

        root.leaves.put(path, new MenuBranch(path, canAddMenuItem));
    }

    private void applyHistory(ComponentCase componentCase) {
            history().pushState(history().currentToken().replaceAllFragments(componentCase.getHistoryToken()).value());
    }

    private MenuBranch getOrAddMenuBranch(MenuBranch root, String pathElement) {
        if (!root.leaves.containsKey(pathElement)) {
            CanAddMenuItem menuItem = root.menuItem.addMenuItem(pathElement);
            root.leaves.put(pathElement, new MenuBranch(pathElement, menuItem));
        }

        return root.leaves.get(pathElement);
    }

    private void addRootPage(ComponentCase componentCase) {
        CanAddMenuItem canAddMenuItem;
        if (componentCase.hasContent()) {
            canAddMenuItem = menuContext.get().addMenuItem(componentCase.getMenuPath(), componentCase.getIconName(), () -> {
                applyHistory(componentCase);
                showPage(componentCase);
            });
        } else {
            canAddMenuItem = menuContext.get().addMenuItem(componentCase.getMenuPath(), componentCase.getIconName());
        }

        roots.put(componentCase.getMenuPath(), new MenuBranch(componentCase.getMenuPath(), canAddMenuItem));
    }

    private void showPage(ComponentCase componentCase) {
        if(nonNull(currentSample))
            currentSample.onComponentRemoved().onBeforeRemove();
        view.clear();
        layoutContext.get().getLayout().hideLeftPanel();
        view.showContent(componentCase.getContent());
        view.scrollTop();
        componentCase.onComponentRevealed().onRevealed();
        currentSample=componentCase;
    }

    private class NoRootMenuException extends RuntimeException {
        public NoRootMenuException(String message) {
            super(message);
        }
    }
}