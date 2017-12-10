package com.subashpoudel.dateview;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateView extends LinearLayout implements TextView.OnEditorActionListener, View.OnKeyListener {

    public static final String DATE_SEPARATOR = "-";
    public static final int DATE_FORMAT_YMD = 0;
    public static final int DATE_FORMAT_YDM = 1;
    public static final int DATE_FORMAT_DMY = 2;
    public static final int DATE_FORMAT_DYM = 3;
    public static final int DATE_FORMAT_MDY = 4;
    public static final int DATE_FORMAT_MYD = 5;

    private EditText editTextYear;
    private EditText editTextMonth;
    private EditText editTextDay;
    private boolean enableErrorFeedback = false;
    private ArrayList<TextView> nextEditTextArray;

    private boolean yearSet = false;
    private boolean monthSet = false;
    private boolean daySet = false;

    @ColorInt
    private int selectedColor = Color.BLACK;
    @ColorInt
    private int errorColor = Color.RED;
    private int dateFormat = DATE_FORMAT_YMD;
    private Date minDate;
    private Date maxDate;
    private DateEnteredListener dateEnteredListener;

    public DateView(Context context) {
        this(context, null);
    }

    public DateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    // View setup

    public void init(AttributeSet attributeSet) {
        nextEditTextArray = new ArrayList<>(3);
        extractXMLAttributes(attributeSet);
        setupView();
    }

    private void extractXMLAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DateView,
                0, 0);

        try {
            selectedColor = a.getColor(R.styleable.DateView_sp_normal_color, Color.BLACK);
            errorColor = a.getColor(R.styleable.DateView_sp_error_color, Color.RED);
            enableErrorFeedback = a.getBoolean(R.styleable.DateView_sp_enable_error_feedback, false);
            dateFormat = a.getInt(R.styleable.DateView_sp_date_format, 0);
            String minDateStr = a.getString(R.styleable.DateView_sp_minDate);
            String maxDateStr = a.getString(R.styleable.DateView_sp_maxDate);
            minDate = validateAndReturnDate(minDateStr);
            maxDate = validateAndReturnDate(maxDateStr);
            validateMinAndMaxDates();
        } finally {
            a.recycle();
        }
    }

    private void setupView() {
        setOrientation(HORIZONTAL);
        setDateFormat(dateFormat);
    }

    public int getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(int dateFormat) {
        nextEditTextArray.clear();
        switch (dateFormat) {
            case DATE_FORMAT_YMD:
                setupYearView();
                setupMonthView();
                setupDayView();
                break;
            case DATE_FORMAT_YDM:
                setupYearView();
                setupDayView();
                setupMonthView();
                break;
            case DATE_FORMAT_DMY:
                setupDayView();
                setupMonthView();
                setupYearView();
                break;
            case DATE_FORMAT_DYM:
                setupDayView();
                setupYearView();
                setupMonthView();
                break;
            case DATE_FORMAT_MDY:
                setupMonthView();
                setupDayView();
                setupYearView();
                break;
            case DATE_FORMAT_MYD:
                setupMonthView();
                setupYearView();
                setupDayView();
                break;
            default:
                setupYearView();
                setupDayView();
                setupMonthView();
                break;
        }
        hideSeparatorFromLastView();
        setupEditTextAutoSwitch();
    }

    private void setupYearView() {
        View yearView = inflate(getContext(), R.layout.item_year, this);
        editTextYear = yearView.findViewById(R.id.et_year);
        editTextYear.setTextColor(selectedColor);
        editTextYear.setOnEditorActionListener(this);
        editTextYear.setTextColor(selectedColor);
        editTextYear.setOnKeyListener(this);
        nextEditTextArray.add(editTextYear);
    }

    private void setupDayView() {
        View dayView = inflate(getContext(), R.layout.item_day, this);
        editTextDay = dayView.findViewById(R.id.et_day);
        editTextDay.setTextColor(selectedColor);
        editTextDay.setOnEditorActionListener(this);
        editTextDay.setTextColor(selectedColor);
        editTextDay.setOnKeyListener(this);
        nextEditTextArray.add(editTextDay);
    }

    public void setupMonthView() {
        View monthView = inflate(getContext(), R.layout.item_month, this);
        editTextMonth = monthView.findViewById(R.id.et_month);
        editTextMonth.setTextColor(selectedColor);
        editTextMonth.setOnEditorActionListener(this);
        editTextMonth.setTextColor(selectedColor);
        editTextMonth.setOnKeyListener(this);
        nextEditTextArray.add(editTextMonth);
    }

    private void hideSeparatorFromLastView() {
        int childCount = getChildCount();
        if (childCount > 0) {
            View parent = getChildAt(childCount - 1);
            parent.findViewById(R.id.tv_separator).setVisibility(GONE);
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            int currentEditTextIndex = nextEditTextArray.indexOf(v);
            if (currentEditTextIndex < 0) {
                return false;
            }
            currentEditTextIndex++;
            if (currentEditTextIndex >= 1 && currentEditTextIndex <= 2) {
                nextEditTextArray.get(currentEditTextIndex).requestFocus();
                return true;
            }
        }
        return false;
    }

    private void setupEditTextAutoSwitch() {
        editTextYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                yearSet = true;
                onDateEntered();
                if (s != null && s.length() == 4) {
                    moveToNextEditText(editTextYear);
                }
            }
        });

        editTextDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                daySet = true;
                onDateEntered();
                if (s != null && s.length() == 2) {
                    moveToNextEditText(editTextDay);
                }
            }
        });

        editTextMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                monthSet = true;
                onDateEntered();
                if (s != null && s.length() == 2) {
                    moveToNextEditText(editTextMonth);
                }
            }
        });

    }

    // for feature moving to previous edittext when current edittext is empty
    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if(keyCode == KeyEvent.KEYCODE_DEL) {
            EditText editText = (EditText) view;
            int index = nextEditTextArray.indexOf(editText);
            if(index > 0 && TextUtils.isEmpty(editText.getText())) {
                nextEditTextArray.get(--index).requestFocus();
            }
        }
        return false;
    }

    private void onDateEntered() {
        Date date = getDate();
        boolean isDateValid = isDateValid(date);
        notifyListeners(date, isDateValid);
        showErrorColor(!isDateValid);
    }

    private void notifyListeners(Date date, boolean isValid) {
        if (dateEnteredListener != null) {
            dateEnteredListener.onDateEntered(date, isValid);
        }
    }

    private void moveToNextEditText(EditText currentEditText) {
        int currentEditTextIndex = nextEditTextArray.indexOf(currentEditText);
        if (currentEditTextIndex < 0) {
            return;
        }
        currentEditTextIndex++;
        if (currentEditTextIndex >= 1 && currentEditTextIndex <= 2) {
            nextEditTextArray.get(currentEditTextIndex).requestFocus();
        }
    }

    private void showErrorColor(boolean show) {
        if (enableErrorFeedback
                && yearSet
                && monthSet
                && daySet) {
            int color = show ? errorColor : selectedColor;
            editTextYear.setTextColor(color);
            editTextMonth.setTextColor(color);
            editTextDay.setTextColor(color);
        }
    }

    public void setDate(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        editTextYear.setText(calendar.get(Calendar.YEAR));
        editTextMonth.setText(calendar.get(Calendar.MONTH));
        editTextDay.setText(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public @Nullable
    Date getDate() {
        if (!isAllTextFieldsDataSet()) {
            return null;
        }
        try {
            String dateStr = editTextYear.getText().toString()
                    + DATE_SEPARATOR
                    + editTextMonth.getText().toString()
                    + DATE_SEPARATOR
                    + editTextDay.getText().toString();
            return validateAndReturnDate(dateStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Validation codes

    private boolean isAllTextFieldsDataSet() {
        return !TextUtils.isEmpty(editTextYear.getText())
                && !TextUtils.isEmpty(editTextMonth.getText())
                && !TextUtils.isEmpty(editTextDay.getText());
    }

    private void validateMinAndMaxDates() {
        if (minDate != null && maxDate != null) {
            if (maxDate.before(minDate)) {
                logError("Error: Max date is set before min date. Setting both dates to null");
                maxDate = null;
                minDate = null;
            }
        }
    }

    private boolean isDateValid(Date date) {
        if (date == null) {
            return false;
        }
        if (minDate != null) {
            if (date.before(minDate)) {
                return false;
            }
        }
        if (maxDate != null) {
            if (date.after(maxDate)) {
                return false;
            }
        }
        return true;
    }

    private Date validateAndReturnDate(String dateStr) {
        try {
            DateComponent dateComponent = new DateComponent(dateStr, DATE_SEPARATOR);
            if (DateUtil.validateUserData(dateComponent)) {
                return DateUtil.getDate(dateStr, DateUtil.YYYY_MM_DD);
            }

        } catch (InvalidDateException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Additional functions

    public void clearDateFields() {
        editTextYear.setText("");
        editTextMonth.setText("");
        editTextDay.setText("");
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
        validateMinAndMaxDates();
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
        validateMinAndMaxDates();
    }

    public void setDateEnteredListener(DateEnteredListener dateEnteredListener) {
        this.dateEnteredListener = dateEnteredListener;
    }

    // Logging

    private void logError(@NonNull String message) {
        Log.e(this.getClass().getName(), message);
    }

    // Interface to notify view on date changes
    public interface DateEnteredListener {
        void onDateEntered(Date date, boolean isValid);
    }

}
