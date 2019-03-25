# DateView
This library is an alternative to the native date picker provided by android. Though it is great, some of our users were having problems providing date especially **year selection** with the date picker.

## Default date picker

![Default date picker](/images/date-picker-default.png)

## Date View

![Date View](/images/date-view.gif)

## Usage XML

```
<com.subashpoudel.dateview.DateView
        android:id="@+id/dateView"
        android:layout_below="@id/text"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:sp_date_format="DMY"
        app:sp_enable_error_feedback="true"
        app:sp_error_color="@android:color/holo_red_dark"
        app:sp_maxDate="2100-01-01"
        app:sp_minDate="1900-01-01"
        app:sp_normal_color="@android:color/holo_orange_dark" />
```
## Usage Java

Register a date change listener to get the last inputted date. 

```
dateView.setDateEnteredListener(new DateView.DateEnteredListener() {
            @Override
            public void onDateEntered(Date date, boolean isValid) {
                if (isValid) {
                    textView.setText(date.toString());
                    textView.setTextColor(Color.BLACK);
                } else {
                    textView.setText("Invalid Date Entered");
                    textView.setTextColor(Color.RED);
                }
            }
        });
```

## Date format

The date format changes the order of the values user inputs. If the format is DMY then user provides the Day first followed by the Month and the Year.

## Available Date Formats

```
YMD
YDM
DMY
DYM
MDY
MYD
```

## Date validation

You can provide maximum and minimum dates to the picker inclusive. The date format is YYYY-MM-DD. If the date is beyond this range, entered date is highlighted with error color.




