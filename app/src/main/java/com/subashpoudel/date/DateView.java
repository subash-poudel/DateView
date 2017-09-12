package com.subashpoudel.date;


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

import com.subashpoudel.R;

import java.util.ArrayList;
import java.util.Date;

public class DateView extends LinearLayout implements TextView.OnEditorActionListener {

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

    private ArrayList<EditText> nextEditTextArray;

    @ColorInt
    private int selectedColor;
    @ColorInt
    private int errorColor;
    private String typeFace;
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

    public void setDateEnteredListener(DateEnteredListener dateEnteredListener) {
        this.dateEnteredListener = dateEnteredListener;
    }

    public void init(AttributeSet attributeSet) {
        nextEditTextArray = new ArrayList<>(3);
        extractXMLAttributes(attributeSet);
        setupView();
    }

    public void setDate(@NonNull Date date) {
        date.
    }

    public void setDate(CharSequence year, CharSequence month, CharSequence day) {
        String dateStr = year
                + DATE_SEPARATOR
                + month
                + DATE_SEPARATOR
                + day;
        Date date = validateAndReturnDate(dateStr);
        if (date != null) {
            editTextYear.setText(year);
            editTextMonth.setText(month);
            editTextDay.setText(day);
        } else {
            logError("Invalid date provided " + dateStr);
        }
    }

    public void clearDate() {
        editTextYear.setText("");
        editTextMonth.setText("");
        editTextDay.setText("");
    }

    private void extractXMLAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DateView,
                0, 0);

        try {
            selectedColor = a.getColor(R.styleable.DateView_sp_normal_color, Color.BLACK);
            errorColor = a.getColor(R.styleable.DateView_sp_normal_color, Color.RED);
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

    private void validateMinAndMaxDates() {
        if (minDate != null && maxDate != null) {
            if (maxDate.before(minDate)) {
                logError("Error: Max date is set before min date. Setting both dates to null");
                maxDate = null;
                minDate = null;
            }
        }
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

    private void logError(String errorMessage) {
        if (errorMessage == null) {
            return;
        }
        Log.e(this.getClass().getName(), errorMessage);
    }

    private void hideSeparetorFromLastView() {
        int childCount = getChildCount();
        if (childCount > 0) {
            View parent = getChildAt(childCount - 1);
            parent.findViewById(R.id.tv_separator).setVisibility(GONE);
        }
    }

    private void setupView() {
        setOrientation(HORIZONTAL);
        setupDateFormat(dateFormat);
    }

    public int getDateFormat() {
        return dateFormat;
    }

    public void setupDateFormat(int dateFormat) {
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
        hideSeparetorFromLastView();
        setupEditTextAutoSwitch();
    }

    private void setupYearView() {
        View yearView = inflate(getContext(), R.layout.item_year, this);
        editTextYear = (EditText) yearView.findViewById(R.id.et_year);
        editTextYear.setTextColor(selectedColor);
        editTextYear.setOnEditorActionListener(this);
        nextEditTextArray.add(editTextYear);
    }

    private void setupDayView() {
        View dayView = inflate(getContext(), R.layout.item_day, this);
        editTextDay = (EditText) dayView.findViewById(R.id.et_day);
        editTextDay.setTextColor(selectedColor);
        editTextDay.setOnEditorActionListener(this);
        nextEditTextArray.add(editTextDay);
    }

    public void setupMonthView() {
        View monthView = inflate(getContext(), R.layout.item_month, this);
        editTextMonth = (EditText) monthView.findViewById(R.id.et_month);
        editTextMonth.setTextColor(selectedColor);
        editTextMonth.setOnEditorActionListener(this);
        nextEditTextArray.add(editTextMonth);
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
                notifyListeners();
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
                notifyListeners();
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
                notifyListeners();
                if (s != null && s.length() == 2) {
                    moveToNextEditText(editTextMonth);
                }
            }
        });


    }

    private void moveToNextEditText(EditText currentEditText) {
        int currentEditTextIndex = nextEditTextArray.indexOf(currentEditText);
        if (currentEditTextIndex < 0) {
            return;
        }
        currentEditTextIndex++;
        if (currentEditTextIndex >= 1 && currentEditTextIndex < nextEditTextArray.size()) {
            nextEditTextArray.get(currentEditTextIndex).requestFocus();
        }
    }

    private boolean isAllTextFieldsDataSet() {
        return !TextUtils.isEmpty(editTextYear.getText())
                && !TextUtils.isEmpty(editTextMonth.getText())
                && !TextUtils.isEmpty(editTextDay.getText());
    }

    public Date getCurrentDate() {
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

    private void notifyListeners() {
        if (dateEnteredListener != null) {
            Date date = getCurrentDate();
            boolean isDateValid = isDateValid(date);
            dateEnteredListener.onDateEntered(date, isDateValid);
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

    public interface DateEnteredListener {
        void onDateEntered(Date date, boolean isValid);
    }

}
