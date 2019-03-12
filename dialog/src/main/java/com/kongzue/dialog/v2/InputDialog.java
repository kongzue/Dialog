package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.listener.OnDismissListener;
import com.kongzue.dialog.util.BlurView;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.util.KongzueDialogHelper;
import com.kongzue.dialog.util.ModalBaseDialog;
import com.kongzue.dialog.util.TextInfo;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.kongzue.dialog.v2.DialogSettings.*;

public class InputDialog extends ModalBaseDialog {
    
    private InputDialog inputDialog;
    private AlertDialog alertDialog;
    private boolean isCanCancel = false;
    private InputInfo inputInfo;
    private int style = -1;
    
    private TextInfo customTitleTextInfo;
    private TextInfo customContentTextInfo;
    private TextInfo customButtonTextInfo;
    private TextInfo customOkButtonTextInfo;
    
    private Context context;
    private String title;
    private String message;
    private String defaultInputText = "";
    private String defaultInputHint = "";
    private String okButtonCaption = "确定";
    private String cancelButtonCaption = "取消";
    private InputDialogOkButtonClickListener onOkButtonClickListener;
    private DialogInterface.OnClickListener onCancelButtonClickListener;
    
    private InputDialog() {
    }
    
    //Fast Function
    public static InputDialog show(Context context, String title, String message, InputDialogOkButtonClickListener onOkButtonClickListener) {
        InputDialog inputDialog = build(context, title, message, "确定", onOkButtonClickListener, "取消", null);
        inputDialog.showDialog();
        return inputDialog;
    }
    
    public static InputDialog show(Context context, String title, String message, String okButtonCaption, InputDialogOkButtonClickListener onOkButtonClickListener,
                                   String cancelButtonCaption, DialogInterface.OnClickListener onCancelButtonClickListener) {
        InputDialog inputDialog = build(context, title, message, okButtonCaption, onOkButtonClickListener, cancelButtonCaption, onCancelButtonClickListener);
        inputDialog.showDialog();
        return inputDialog;
    }
    
    public static InputDialog build(Context context, String title, String message, String okButtonCaption, InputDialogOkButtonClickListener onOkButtonClickListener,
                                    String cancelButtonCaption, DialogInterface.OnClickListener onCancelButtonClickListener) {
        synchronized (InputDialog.class) {
            InputDialog inputDialog = new InputDialog();
            inputDialog.cleanDialogLifeCycleListener();
            inputDialog.alertDialog = null;
            inputDialog.context = context;
            inputDialog.title = title;
            inputDialog.message = message;
            inputDialog.okButtonCaption = okButtonCaption;
            inputDialog.cancelButtonCaption = cancelButtonCaption;
            inputDialog.onOkButtonClickListener = onOkButtonClickListener;
            inputDialog.onCancelButtonClickListener = onCancelButtonClickListener;
            inputDialog.isCanCancel = dialog_cancelable_default;
            inputDialog.log("装载输入对话框 -> " + message);
            inputDialog.inputDialog = inputDialog;
            modalDialogList.add(inputDialog);
            return inputDialog;
        }
    }
    
    private BlurView blur;
    private ViewGroup bkg;
    private TextView txtDialogTitle;
    private TextView txtDialogTip;
    private EditText txtInput;
    private ImageView splitHorizontal;
    private TextView btnSelectNegative;
    private ImageView splitVertical;
    private TextView btnSelectPositive;
    private RelativeLayout customView;
    
    int blur_front_color;
    
    private KongzueDialogHelper kongzueDialogHelper;
    
    public void showDialog() {
        if (customTitleTextInfo == null) {
            customTitleTextInfo = dialogTitleTextInfo;
        }
        if (customContentTextInfo == null) {
            customContentTextInfo = dialogContentTextInfo;
        }
        if (customButtonTextInfo == null) {
            customButtonTextInfo = dialogButtonTextInfo;
        }
        if (customOkButtonTextInfo == null) {
            if (dialogOkButtonTextInfo == null) {
                customOkButtonTextInfo = customButtonTextInfo;
            } else {
                customOkButtonTextInfo = dialogOkButtonTextInfo;
            }
        }
        
        dialogList.add(inputDialog);
        log("启动输入对话框 -> " + message);
        if (style == -1) style = DialogSettings.style;
        modalDialogList.remove(inputDialog);
        AlertDialog.Builder builder;
        
        switch (style) {
            case STYLE_IOS:
                switch (dialog_theme) {
                    case THEME_DARK:
                        builder = new AlertDialog.Builder(context, R.style.darkMode);
                        break;
                    default:
                        builder = new AlertDialog.Builder(context, R.style.lightMode);
                        break;
                }
                break;
            case STYLE_MATERIAL:
                if (dialog_theme == THEME_DARK) {
                    builder = new AlertDialog.Builder(context, R.style.materialDialogDark);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                break;
            case STYLE_KONGZUE:
                switch (dialog_theme) {
                    case THEME_DARK:
                        builder = new AlertDialog.Builder(context, R.style.materialDialogDark);
                        break;
                    default:
                        builder = new AlertDialog.Builder(context, R.style.materialDialogLight);
                        break;
                }
                break;
            default:
                builder = new AlertDialog.Builder(context);
                break;
        }
        
        alertDialog = builder.create();
        alertDialog.setView(new EditText(context));
        if (getDialogLifeCycleListener() != null)
            getDialogLifeCycleListener().onCreate(alertDialog);
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);
      
        View rootView;
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        kongzueDialogHelper = new KongzueDialogHelper().setAlertDialog(alertDialog, new OnDismissListener() {
            @Override
            public void onDismiss() {
                dialogList.remove(inputDialog);
                if (bkg != null) bkg.removeAllViews();
                if (kongzueDialogHelper != null) kongzueDialogHelper.dismiss();
                if (customView != null) customView.removeAllViews();
                if (onCancelButtonClickListener != null)
                    onCancelButtonClickListener.onClick(alertDialog, BUTTON_NEGATIVE);
                if (getDialogLifeCycleListener() != null) getDialogLifeCycleListener().onDismiss();
                isDialogShown = false;
    
                if (!modalDialogList.isEmpty()) {
                    showNextModalDialog();
                }
                context = null;
            }
        });
        
        Window window = alertDialog.getWindow();
        switch (style) {
            case STYLE_KONGZUE:
                rootView = LayoutInflater.from(context).inflate(R.layout.dialog_select, null);
                alertDialog.setView(rootView);
                kongzueDialogHelper.show(fragmentManager, "kongzueDialog");
                
                bkg = (LinearLayout) rootView.findViewById(R.id.bkg);
                txtDialogTitle = rootView.findViewById(R.id.txt_dialog_title);
                txtDialogTip = rootView.findViewById(R.id.txt_dialog_tip);
                txtInput = rootView.findViewById(R.id.txt_input);
                btnSelectNegative = rootView.findViewById(R.id.btn_selectNegative);
                btnSelectPositive = rootView.findViewById(R.id.btn_selectPositive);
                customView = rootView.findViewById(R.id.box_custom);
                
                if (inputInfo != null) {
                    txtInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(inputInfo.getMAX_LENGTH())});
                    txtInput.setInputType(inputInfo.getInputType());
                }
                
                if (isNull(title)) {
                    txtDialogTitle.setVisibility(View.GONE);
                } else {
                    txtDialogTitle.setVisibility(View.VISIBLE);
                    txtDialogTitle.setText(title);
                }
                if (isNull(message)) {
                    txtDialogTip.setVisibility(View.GONE);
                } else {
                    txtDialogTip.setVisibility(View.VISIBLE);
                    txtDialogTip.setText(message);
                }
                
                if (dialog_input_text_size > 0) {
                    txtInput.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dialog_input_text_size);
                }
                
                txtInput.setVisibility(View.VISIBLE);
                txtInput.setText(defaultInputText);
                txtInput.setHint(defaultInputHint);
                
                btnSelectNegative.setVisibility(View.VISIBLE);
                btnSelectPositive.setText(okButtonCaption);
                btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setIMMStatus(false, txtInput);
                        if (onOkButtonClickListener != null)
                            onOkButtonClickListener.onClick(alertDialog, txtInput.getText().toString());
                        onCancelButtonClickListener = null;
                    }
                });
                btnSelectNegative.setText(cancelButtonCaption);
                btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kongzueDialogHelper.dismiss();
                        if (onCancelButtonClickListener != null)
                            onCancelButtonClickListener.onClick(alertDialog, BUTTON_NEGATIVE);
                        onCancelButtonClickListener = null;
                    }
                });
                
                if (dialog_theme == THEME_DARK) {
                    bkg.setBackgroundResource(R.color.dlg_bkg_dark);
                    btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_kongzue_gray_dark);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_kongzue_blue_dark);
                    btnSelectNegative.setTextColor(Color.rgb(255, 255, 255));
                    btnSelectPositive.setTextColor(Color.rgb(255, 255, 255));
                    txtInput.setTextColor(Color.rgb(255, 255, 255));
                    txtInput.setBackgroundResource(R.drawable.editbox_bkg_dark);
                }
    
                useTextInfo(txtDialogTitle, customTitleTextInfo);
                useTextInfo(txtDialogTip, customContentTextInfo);
                useTextInfo(btnSelectNegative, customButtonTextInfo);
                useTextInfo(btnSelectPositive, customOkButtonTextInfo);
                
                if (dialog_background_color != -1) {
                    bkg.setBackgroundResource(dialog_background_color);
                }
                
                break;
            case STYLE_MATERIAL:
                kongzueDialogHelper.show(fragmentManager, "kongzueDialog");
                
                txtInput = new EditText(context);
                txtInput.setSingleLine();
                txtInput.post(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) txtInput.getLayoutParams();
                        p.setMargins(dip2px(context, 20), 0, dip2px(context, 20), 0);
                        txtInput.requestLayout();
                    }
                });
                txtInput.setText(defaultInputText);
                txtInput.setHint(defaultInputHint);
                
                if (inputInfo != null) {
                    txtInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(inputInfo.getMAX_LENGTH())});
                    txtInput.setInputType(inputInfo.getInputType());
                }
                
                alertDialog.setTitle(title);
                alertDialog.setMessage(message);
                alertDialog.setView(txtInput);
                alertDialog.setButton(BUTTON_POSITIVE, okButtonCaption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
        
                    }
                });
                alertDialog.setButton(BUTTON_NEGATIVE, cancelButtonCaption, onCancelButtonClickListener);
                
                if (customView != null) alertDialog.setView(customView);
                
                if (dialog_theme == THEME_DARK) {
                    txtInput.setTextColor(Color.rgb(255, 255, 255));
                } else {
                    txtInput.setTextColor(Color.rgb(0, 0, 0));
                }
                if (dialog_background_color != -1) {
                    alertDialog.getWindow().getDecorView().setBackgroundResource(dialog_background_color);
                }
    
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onOkButtonClickListener != null)
                                    onOkButtonClickListener.onClick(alertDialog, txtInput.getText().toString());
                                onCancelButtonClickListener = null;
                                
                                if (txtInput!=null){
                                    txtInput.setFocusable(true);
                                    txtInput.setFocusableInTouchMode(true);
                                    txtInput.requestFocus();
                                    InputMethodManager inputManager = (InputMethodManager) txtInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputManager.showSoftInput(txtInput, 0);
                                }
                            }
                        });
                    }
                });
                
                break;
            case STYLE_IOS:
                rootView = LayoutInflater.from(context).inflate(R.layout.dialog_select_ios, null);
                alertDialog.setView(rootView);
                kongzueDialogHelper.show(fragmentManager, "kongzueDialog");
                
                window.setWindowAnimations(R.style.iOSAnimStyle);
                bkg = (RelativeLayout) rootView.findViewById(R.id.bkg);
                txtDialogTitle = rootView.findViewById(R.id.txt_dialog_title);
                txtDialogTip = rootView.findViewById(R.id.txt_dialog_tip);
                txtInput = rootView.findViewById(R.id.txt_input);
                splitHorizontal = rootView.findViewById(R.id.split_horizontal);
                btnSelectNegative = rootView.findViewById(R.id.btn_selectNegative);
                splitVertical = rootView.findViewById(R.id.split_vertical);
                btnSelectPositive = rootView.findViewById(R.id.btn_selectPositive);
                txtInput = rootView.findViewById(R.id.txt_input);
                customView = rootView.findViewById(R.id.box_custom);
                
                if (inputInfo != null) {
                    txtInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(inputInfo.getMAX_LENGTH())});
                    txtInput.setInputType(inputInfo.getInputType());
                }
                
                ImageView splitVertical = rootView.findViewById(R.id.split_vertical);
                splitVertical.setVisibility(View.VISIBLE);
                txtInput.setVisibility(View.VISIBLE);
                txtInput.setText(defaultInputText);
                txtInput.setHint(defaultInputHint);
                
                if (isNull(title)) {
                    txtDialogTitle.setVisibility(View.GONE);
                } else {
                    txtDialogTitle.setVisibility(View.VISIBLE);
                    txtDialogTitle.setText(title);
                }
                if (isNull(message)) {
                    txtDialogTip.setVisibility(View.GONE);
                } else {
                    txtDialogTip.setVisibility(View.VISIBLE);
                    txtDialogTip.setText(message);
                }
                
                useTextInfo(txtDialogTitle, customTitleTextInfo);
                useTextInfo(txtDialogTip, customContentTextInfo);
                useTextInfo(btnSelectNegative, customButtonTextInfo);
                useTextInfo(btnSelectPositive, customOkButtonTextInfo);
                
                if (dialog_input_text_size > 0) {
                    txtInput.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dialog_input_text_size);
                }
                
                btnSelectPositive.setText(okButtonCaption);
                btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onOkButtonClickListener != null)
                            onOkButtonClickListener.onClick(alertDialog, txtInput.getText().toString());
                        onCancelButtonClickListener = null;
                    }
                });
                btnSelectNegative.setVisibility(View.VISIBLE);
                btnSelectNegative.setText(cancelButtonCaption);
                btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (onCancelButtonClickListener != null)
                            onCancelButtonClickListener.onClick(alertDialog, BUTTON_NEGATIVE);
                        onCancelButtonClickListener = null;
                    }
                });
                
                int bkgResId;
                if (dialog_theme == THEME_DARK) {
                    splitHorizontal.setBackgroundResource(R.color.ios_dialog_split_dark);
                    splitVertical.setBackgroundResource(R.color.ios_dialog_split_dark);
                    btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left_dark);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right_dark);
                    txtInput.setTextColor(Color.rgb(255, 255, 255));
                    txtInput.setBackgroundResource(R.drawable.editbox_bkg_ios_dark);
                    bkgResId = R.drawable.rect_dlg_dark;
                    blur_front_color = Color.argb(blur_alpha, 0, 0, 0);
                } else {
                    btnSelectNegative.setBackgroundResource(R.drawable.button_dialog_left);
                    btnSelectPositive.setBackgroundResource(R.drawable.button_dialog_right);
                    txtInput.setTextColor(Color.rgb(0, 0, 0));
                    txtInput.setBackgroundResource(R.drawable.editbox_bkg_ios);
                    bkgResId = R.drawable.rect_light;
                    blur_front_color = Color.argb(blur_alpha, 255, 255, 255);      //白
                }
                
                if (use_blur) {
                    bkg.post(new Runnable() {
                        @Override
                        public void run() {
                            blur = new BlurView(context, null);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bkg.getHeight());
                            blur.setOverlayColor(blur_front_color);
                            bkg.addView(blur, 0, params);
                        }
                    });
                } else {
                    bkg.setBackgroundResource(bkgResId);
                }
                if (dialog_background_color != -1) {
                    bkg.setBackgroundResource(dialog_background_color);
                }
                break;
        }
        isDialogShown = true;
        if (getDialogLifeCycleListener() != null) getDialogLifeCycleListener().onShow(alertDialog);
        kongzueDialogHelper.setCancelable(isCanCancel);
    }
    
    private void useTextInfo(TextView textView, TextInfo textInfo) {
        if (textInfo.getFontSize() > 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textInfo.getFontSize());
        }
        if (textInfo.getFontColor() != 1) {
            textView.setTextColor(textInfo.getFontColor());
        }
        if (textInfo.getGravity() != -1) {
            textView.setGravity(textInfo.getGravity());
        }
        Typeface font = Typeface.create(Typeface.SANS_SERIF, textInfo.isBold()?Typeface.BOLD:Typeface.NORMAL);
        textView.setTypeface(font);
    }
    
    @Override
    public void doDismiss() {
        if (kongzueDialogHelper != null) kongzueDialogHelper.dismiss();
    }
    
    public InputDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (kongzueDialogHelper != null) kongzueDialogHelper.setCancelable(canCancel);
        return this;
    }
    
    public InputDialog setDefaultInputText(String defaultInputText) {
        this.defaultInputText = defaultInputText;
        if (alertDialog != null) {
            txtInput.setText(defaultInputText);
            txtInput.setHint(defaultInputHint);
        }
        return this;
    }
    
    public InputDialog setDefaultInputHint(String defaultInputHint) {
        this.defaultInputHint = defaultInputHint;
        if (alertDialog != null) {
            txtInput.setText(defaultInputText);
            txtInput.setHint(defaultInputHint);
        }
        return this;
    }
    
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    public InputDialog setCustomView(View view) {
        if (style == STYLE_MATERIAL) {
            customView = new RelativeLayout(context);
            customView.addView(view);
        } else {
            if (alertDialog != null && view != null) {
                customView.setVisibility(View.VISIBLE);
                customView.addView(view);
            }
        }
        return this;
    }
    
    private boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || s.equals("null")) {
            return true;
        }
        return false;
    }
    
    private void setIMMStatus(boolean show, EditText editText) {
        if (show) {
            editText.requestFocus();
            editText.setFocusableInTouchMode(true);
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        } else {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
    
    //输入内容设置
    public InputDialog setInputInfo(InputInfo inputInfo) {
        if (txtInput != null) {
            txtInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(inputInfo.getMAX_LENGTH())});
            txtInput.setInputType(InputType.TYPE_CLASS_TEXT | inputInfo.getInputType());
        }
        this.inputInfo = inputInfo;
        return this;
    }
    
    public InputDialog setTitleTextInfo(TextInfo textInfo) {
        this.customTitleTextInfo = textInfo;
        return this;
    }
    
    public InputDialog setContentTextInfo(TextInfo textInfo) {
        this.customContentTextInfo = textInfo;
        return this;
    }
    
    public InputDialog setButtonTextInfo(TextInfo textInfo) {
        this.customButtonTextInfo = textInfo;
        return this;
    }
    
    public InputDialog setOkButtonTextInfo(TextInfo textInfo) {
        this.customOkButtonTextInfo = textInfo;
        return this;
    }
    
    public InputDialog setDialogStyle(int style) {
        this.style = style;
        return this;
    }
    
    public AlertDialog getAlertDialog() {
        return alertDialog;
    }
}
