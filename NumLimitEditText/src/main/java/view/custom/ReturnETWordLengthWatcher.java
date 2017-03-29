package view.custom;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * EditText字符长度限制
 */
class ReturnETWordLengthWatcher implements TextWatcher {
    private EditText               editText;
    private int                    maxLen;
    private Context                mContext;
    private NotifyLeaveEditTextNum notifyLeaveEditTextNum;

    ReturnETWordLengthWatcher(int maxLen, EditText editText, Context context, NotifyLeaveEditTextNum notifyEditTextNum) {
        this.maxLen = maxLen;
        this.editText = editText;
        this.mContext = context;
        this.notifyLeaveEditTextNum = notifyEditTextNum;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int editStart = editText.getSelectionStart();
        int editEnd = editText.getSelectionEnd();

        // 先去掉监听器，否则会出现栈溢出
        editText.removeTextChangedListener(this);

        // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
        // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
        try {
            while (getWordCount(s.toString()) > maxLen) { // 当输入字符个数超过限制的大小时，进行截断操作
                if (editStart <= 0) {
                    break;
                }
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);//如果输入法在窗口上已经显示，则隐藏，反之则显示
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // mEditText.setText(s);将这行代码注释掉就不会出现后面所说的输入法在数字界面自动跳转回主界面的问题了
        editText.setSelection(editStart);
        notifyLeaveEditTextNum.getEditTextNum(maxLen - getWordCount(s.toString()));
        // 恢复监听器
        editText.addTextChangedListener(this);
    }

    //获取字符串的字节长度
    private int getWordCount(String s) {
        //s = s.replaceAll("[^\\x00-\\xff]", "**");
        //return s.length();
        return s.toCharArray().length;
    }

    interface NotifyLeaveEditTextNum {
        void getEditTextNum(int num);
    }
}
