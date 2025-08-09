package com.android.mail.compose;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.android.mail.R;
import com.android.mail.ui.OnePaneController;

public class BottomFullDialog extends Dialog {

    private View contentView;

    public BottomFullDialog(Context context) {
        super(context);
    }

    public BottomFullDialog(Context context, int themeResId, int layout) {
        super(context, themeResId);
        contentView = getLayoutInflater().inflate(layout, null);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(contentView);

    }

    public View getView() {
        if (null != contentView) {
            return contentView;
        }
        return null;
    }

    protected BottomFullDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = display.getWidth();
        if(OnePaneController.MainAndReplyFlag == 1){
            layoutParams.height = 280;
        }else{
            layoutParams.height = 230;
        }
        getWindow().setAttributes(layoutParams);
    }

    public interface DialogOnKeyDownListener {
        void onKeyDownListener(int keyCode, KeyEvent event);
    }

    private DialogOnKeyDownListener dialogOnKeyDownListener;

    public void setDialogOnKeyDownListener(DialogOnKeyDownListener dialogOnKeyDownListener) {
        this.dialogOnKeyDownListener = dialogOnKeyDownListener;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (dialogOnKeyDownListener != null) {
            dialogOnKeyDownListener.onKeyDownListener(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}