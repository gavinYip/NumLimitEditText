package view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by yip on 16/6/1.
 * EditText 带剩余字数
 */
public class NumLimitEditText extends LinearLayout {

    private Context  context;
    public  EditText editText;
    private TextView tvNum;
    private int DEFAULT_MAX_NUM = 100;

    private int    textMaxNum = DEFAULT_MAX_NUM;
    private String hint       = "";
    private int padding;
    private int textColor;
    private int hintTextColor;
    private int textSize;
    private int numTextSize;
    private int height;
    private int minHeight = -1;
    private int maxLines;

    public NumLimitEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumLimitEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumLimitEditText, defStyleAttr, 0);
        textMaxNum = a.getInteger(R.styleable.NumLimitEditText_textMaxNum, DEFAULT_MAX_NUM);
        hint = a.getString(R.styleable.NumLimitEditText_hint);
        padding = a.getDimensionPixelSize(R.styleable.NumLimitEditText_textPadding, 12);
        textColor = a.getColor(R.styleable.NumLimitEditText_textColor, Color.parseColor("#2d2d37"));
        hintTextColor = a.getColor(R.styleable.NumLimitEditText_hintTextColor, Color.parseColor("#b5b4b9"));
        textSize = a.getInteger(R.styleable.NumLimitEditText_textSize, 15);
        numTextSize = a.getInteger(R.styleable.NumLimitEditText_numTextSize, 15);
        height = a.getDimensionPixelSize(R.styleable.NumLimitEditText_editHeight, -1);
        maxLines = a.getInteger(R.styleable.NumLimitEditText_maxLines, -1);
        if (-1 == height) {
            minHeight = a.getDimensionPixelSize(R.styleable.NumLimitEditText_minimumHeight, 160);
        } else {
            minHeight = -1;
        }
        a.recycle();
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.edit_text_limit_num, this);
        editText = (EditText) findViewById(R.id.edit_text);
        tvNum = (TextView) findViewById(R.id.tv_num);
        tvNum.setText(String.valueOf(textMaxNum));
        editText.setHint(hint);
        editText.setPadding(padding, padding, padding, 0);
        tvNum.setPadding(padding, padding, padding, padding);
        if (-1 == minHeight) {
            editText.setHeight(height);
        } else {
            editText.setMinHeight(minHeight);
        }
        editText.setTextColor(textColor);
        editText.setHintTextColor(hintTextColor);
        editText.setTextSize(textSize);
        tvNum.setTextColor(hintTextColor);
        tvNum.setTextSize(numTextSize);
        if (-1 != maxLines) {
            editText.setMaxLines(maxLines);
        }
        editText.addTextChangedListener(new ReturnETWordLengthWatcher(textMaxNum, editText, context,
                new ReturnETWordLengthWatcher.NotifyLeaveEditTextNum() {
                    @Override
                    public void getEditTextNum(int num) {
                        tvNum.setText(String.valueOf(num));
                    }
                }));
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(CharSequence text) {
        editText.setText(text);
    }

    public void addTextChangedListener(TextWatcher l) {
        editText.addTextChangedListener(l);
    }

    public void setHint(CharSequence text) {
        editText.setHint(text);
    }

    public void setOptions(int options) {
        editText.setImeOptions(options);
        editText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        editText.setLines(3);
    }

}
