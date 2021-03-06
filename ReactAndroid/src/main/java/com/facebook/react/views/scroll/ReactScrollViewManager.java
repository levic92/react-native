/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.react.views.scroll;

import javax.annotation.Nullable;

import java.util.Map;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.CatalystStylesDiffMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIProp;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.views.view.ReactClippingViewGroupHelper;

/**
 * View manager for {@link ReactScrollView} components.
 *
 * <p>Note that {@link ReactScrollView} and {@link ReactHorizontalScrollView} are exposed to JS
 * as a single ScrollView component, configured via the {@code horizontal} boolean property.
 */
public class ReactScrollViewManager
    extends ViewGroupManager<ReactScrollView>
    implements ReactScrollViewCommandHelper.ScrollCommandHandler<ReactScrollView> {

  private static final String REACT_CLASS = "RCTScrollView";

  @UIProp(UIProp.Type.BOOLEAN) public static final String PROP_SHOWS_VERTICAL_SCROLL_INDICATOR =
      "showsVerticalScrollIndicator";
  @UIProp(UIProp.Type.BOOLEAN) public static final String PROP_SHOWS_HORIZONTAL_SCROLL_INDICATOR =
      "showsHorizontalScrollIndicator";

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  public ReactScrollView createViewInstance(ThemedReactContext context) {
    return new ReactScrollView(context);
  }

  @Override
  public void updateView(ReactScrollView scrollView, CatalystStylesDiffMap props) {
    super.updateView(scrollView, props);
    if (props.hasKey(PROP_SHOWS_VERTICAL_SCROLL_INDICATOR)) {
      scrollView.setVerticalScrollBarEnabled(
          props.getBoolean(PROP_SHOWS_VERTICAL_SCROLL_INDICATOR, true));
    }

    if (props.hasKey(PROP_SHOWS_HORIZONTAL_SCROLL_INDICATOR)) {
      scrollView.setHorizontalScrollBarEnabled(
          props.getBoolean(PROP_SHOWS_HORIZONTAL_SCROLL_INDICATOR, true));
    }

    ReactClippingViewGroupHelper.applyRemoveClippedSubviewsProperty(scrollView, props);
  }

  @Override
  public @Nullable Map<String, Integer> getCommandsMap() {
    return ReactScrollViewCommandHelper.getCommandsMap();
  }

  @Override
  public void receiveCommand(
      ReactScrollView scrollView,
      int commandId,
      @Nullable ReadableArray args) {
    ReactScrollViewCommandHelper.receiveCommand(this, scrollView, commandId, args);
  }

  @Override
  public void scrollTo(
      ReactScrollView scrollView,
      ReactScrollViewCommandHelper.ScrollToCommandData data) {
    scrollView.smoothScrollTo(data.mDestX, data.mDestY);
  }

  @Override
  public void scrollWithoutAnimationTo(
      ReactScrollView scrollView,
      ReactScrollViewCommandHelper.ScrollToCommandData data) {
    scrollView.scrollTo(data.mDestX, data.mDestY);
  }

  @Override
  public @Nullable Map getExportedCustomDirectEventTypeConstants() {
    return MapBuilder.builder()
        .put(ScrollEvent.EVENT_NAME, MapBuilder.of("registrationName", "onScroll"))
        .put("topScrollBeginDrag", MapBuilder.of("registrationName", "onScrollBeginDrag"))
        .put("topScrollEndDrag", MapBuilder.of("registrationName", "onScrollEndDrag"))
        .put("topScrollAnimationEnd", MapBuilder.of("registrationName", "onScrollAnimationEnd"))
        .put("topMomentumScrollBegin", MapBuilder.of("registrationName", "onMomentumScrollBegin"))
        .put("topMomentumScrollEnd", MapBuilder.of("registrationName", "onMomentumScrollEnd"))
        .build();
  }
}
