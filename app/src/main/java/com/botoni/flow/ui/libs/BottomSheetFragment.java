package com.botoni.flow.ui.libs;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
public abstract class BottomSheetFragment extends Fragment {
    private BottomSheetBehavior<View> behavior;
    private boolean isDismissing = false;
    @NonNull
    protected abstract View getBackgroundView();
    @NonNull
    protected abstract View getBottomSheetView();
    protected abstract void onBind(@NonNull BottomSheetBehavior<View> behavior);

    protected abstract void onStateChanged(@NonNull View bottomSheet, int newState);

    protected void onSlide(@NonNull View bottomSheet, float slideOffset) {
        View background = getBackgroundView();
        background.setAlpha(Math.max(0f, slideOffset));
    }

    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if (isAdded() || manager.findFragmentByTag(tag) != null) return;

        manager.beginTransaction()
                .add(android.R.id.content, this, tag)
                .setReorderingAllowed(true)
                .commitAllowingStateLoss();
    }

    public void dismiss() {
        if (isDismissing) return;
        if (behavior != null && behavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            dismissInternal();
        }
    }

    private void dismissInternal() {
        if (isDismissing || !isAdded()) return;
        isDismissing = true;
        getParentFragmentManager().beginTransaction()
                .remove(this)
                .commitAllowingStateLoss();
    }

    @Override
    @CallSuper
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View sheet = getBottomSheetView();
        View background = getBackgroundView();
        behavior = BottomSheetBehavior.from(sheet);
        behavior.addBottomSheetCallback(new BottomSheetDismissCallback());
        background.setOnClickListener(v -> dismiss());
        onBind(behavior);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheet.post(() -> {
            if (isAdded() && behavior != null) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    @Override
    public void onDestroyView() {
        behavior = null;
        super.onDestroyView();
    }

    private class BottomSheetDismissCallback extends BottomSheetBehavior.BottomSheetCallback {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismissInternal();
            }
            BottomSheetFragment.this.onStateChanged(bottomSheet, newState);
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            BottomSheetFragment.this.onSlide(bottomSheet, slideOffset);
        }
    }
}