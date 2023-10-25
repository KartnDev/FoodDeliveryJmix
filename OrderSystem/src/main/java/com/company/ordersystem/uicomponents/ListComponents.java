package com.company.ordersystem.uicomponents;

import com.company.restaurantapi.model.HasIcon;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import io.jmix.flowui.data.items.ContainerDataProvider;
import io.jmix.flowui.model.CollectionContainer;
import lombok.AllArgsConstructor;

import java.io.ByteArrayInputStream;
import java.util.function.BiConsumer;

@UIScope
@AllArgsConstructor
@org.springframework.stereotype.Component
public final class ListComponents {

    private static final String DEFAULT_ICON = "/META-INF/resources/icons/cutlery.svg";

    public <T extends HasIcon> VirtualList<T> attachListRenderer(HasComponents rootAttachComponent,
                                                                 CollectionContainer<T> collectionContainer,
                                                                 BiConsumer<HasIcon, ListComponentContext> infoLayoutUpdater) {
        rootAttachComponent.removeAll();
        var tVirtualList = new VirtualList<T>();
        tVirtualList.setWidthFull();
        collectionContainer.addCollectionChangeListener(e -> tVirtualList.getDataProvider().refreshAll());
        tVirtualList.setDataProvider(new ContainerDataProvider<>(collectionContainer));
        tVirtualList.getDataProvider().refreshAll();
        tVirtualList.setRenderer(new ComponentRenderer<>(item -> {
            var rootCardLayout = new HorizontalLayout();
            rootCardLayout.setMargin(true);
            rootCardLayout.setSpacing(true);

            var infoLayout = new VerticalLayout();
            infoLayout.setMargin(false);
            infoLayout.setSpacing(false);
            infoLayout.setPadding(false);

            var avatar = createAvatarIconForEntity(item);

            rootCardLayout.add(avatar, infoLayout);

            infoLayoutUpdater.accept(item, new ListComponentContext(rootCardLayout, infoLayout));

            return rootCardLayout;
        }));
        rootAttachComponent.add(tVirtualList);
        return tVirtualList;
    }

    private Avatar createAvatarIconForEntity(HasIcon hasIcon) {
        Avatar avatar = new Avatar();
        avatar.addThemeVariants(AvatarVariant.LUMO_XLARGE);

        if (hasIcon.getIcon() == null) {
            avatar.setImage(DEFAULT_ICON);
            return avatar;
        }

        try {
            avatar.setImageResource(new StreamResource(hasIcon.getAttachmentName(),
                    () -> new ByteArrayInputStream(hasIcon.getIcon())));
        } catch (RuntimeException e) {
            avatar.setImage(DEFAULT_ICON);
        }

        return avatar;
    }

    public record ListComponentContext(HorizontalLayout rootLayout, VerticalLayout infoLayout) {
    }
}
