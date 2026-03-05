package com.botoni.flow.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.botoni.flow.databinding.FragmentSearchBottomSheetBinding;
import com.botoni.flow.libs.BottomSheetFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class SearchBottonSheetFragment extends BottomSheetFragment {
    private FragmentSearchBottomSheetBinding binding;
    private boolean isKeyboardVisible = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    protected View getBackgroundView() {
        return binding.getRoot();
    }

    @NonNull
    @Override
    protected View getBottomSheetView() {
        return binding.bottom;
    }

    @Override
    protected void onBind(@NonNull BottomSheetBehavior<View> behavior) {
        if (binding == null) return;

        int h = getResources().getDisplayMetrics().heightPixels;

        behavior.setHideable(true);
        behavior.setFitToContents(false);
        behavior.setPeekHeight((int) (h * 0.4f));
        behavior.setHalfExpandedRatio(0.8f);
        behavior.setExpandedOffset((int) (h * 0.2f));

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            if (binding == null || !isAdded()) return insets;

            boolean visible = insets.isVisible(WindowInsetsCompat.Type.ime());

            if (visible != isKeyboardVisible) {
                isKeyboardVisible = visible;
                if (visible && behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }
            }
            return insets;
        });
    }

    @Override
    protected void onStateChanged(@NonNull View bottomSheet, int newState) {
        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
            hideKeyboard();
        }

        if (newState == BottomSheetBehavior.STATE_DRAGGING && isKeyboardVisible) {
            hideKeyboard();
        }
    }

    @Override
    protected void onSlide(@NonNull View bottomSheet, float slideOffset) {
        if (binding == null) return;
        float alpha = Math.max(0f, Math.min(1f, slideOffset));
        binding.getRoot().setBackgroundColor(Color.argb((int) (alpha * 150), 0, 0, 0));
        if (slideOffset < 0.5f && isKeyboardVisible) {
            hideKeyboard();
        }
    }

    private void hideKeyboard() {
        if (binding != null && getActivity() != null) {
            WindowCompat.getInsetsController(getActivity().getWindow(), binding.getRoot())
                    .hide(WindowInsetsCompat.Type.ime());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}