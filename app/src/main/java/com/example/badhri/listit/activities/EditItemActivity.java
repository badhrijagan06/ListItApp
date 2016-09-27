package com.example.badhri.listit.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.badhri.listit.R;

import java.util.Calendar;
import java.text.SimpleDateFormat;
/**
 * Created by badhri on 9/22/16.
 */
public class EditItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private int position;
    private EditText itemText;
    private RadioGroup radioPriorityGroup;
    private RadioGroup radioStatusGroup;
    private EditText deadLineText;
    private EditText commentText;
    private DialogFragment newFragment;
    private TextView title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_item_dialog);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar_edit);
        title = (TextView)findViewById(R.id.textView2);
        setSupportActionBar(myToolbar);
        setTitle("");

        itemText = (EditText) findViewById(R.id.enterItemfield);
        radioPriorityGroup = (RadioGroup)findViewById(R.id.radiopriority);
        radioStatusGroup = (RadioGroup) findViewById(R.id.radiostatus);
        deadLineText = (EditText)findViewById(R.id.finishby);
        deadLineText.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {

                    newFragment = new DatePickerFragment();

                    newFragment.show(getFragmentManager(), "datePicker");

                }
                return true;
            }
        });
        commentText = (EditText)findViewById(R.id.commenttext);
        String item = getIntent().getStringExtra("Item");
        title.setText(R.string.additem);
        RadioButton r;
        if (item != null) {
            title.setText(R.string.edititem);
            position = getIntent().getIntExtra("Position", 0);
            itemText.setText(item);
            String priority = getIntent().getStringExtra("Priority");
            if (priority != null) {
                if (priority.contentEquals("Low")) {
                    r = (RadioButton)findViewById(R.id.radioLow);
                    r.setChecked(true);
                } else if (priority.contentEquals("Medium")) {
                    r = (RadioButton)findViewById(R.id.radioMedium);
                    r.setChecked(true);
                } else {
                    r = (RadioButton)findViewById(R.id.radioHigh);
                    r.setChecked(true);
                }
            }

            String status = getIntent().getStringExtra("Status");
            if (status != null) {
                if (status.contentEquals("done")) {
                    r = (RadioButton)findViewById(R.id.radiodone);
                    r.setChecked(true);
                } else {
                    r = (RadioButton)findViewById(R.id.radionotdone);
                    r.setChecked(true);
                }
            }

            String deadline = getIntent().getStringExtra("Deadline");
            if (deadline != null)
                deadLineText.setText(deadline);

            String comments = getIntent().getStringExtra("Comments");
            if (comments != null)
                commentText.setText(comments);



        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());
        deadLineText.setText(formattedDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Listit", "Create options called");
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_save) {
            String text = itemText.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.emptyitem, Toast.LENGTH_LONG);
                setResult(RESULT_CANCELED);
                this.finish();
                return true;
            }

            Intent data = new Intent();
            data.putExtra("Item", text);
            data.putExtra("Position", position);
            data.putExtra("Comments", commentText.getText().toString());
            data.putExtra("Deadline", deadLineText.getText().toString());
            int selectedId = radioPriorityGroup.getCheckedRadioButtonId();
            RadioButton radioPriorityButton = (RadioButton) findViewById(selectedId);
            data.putExtra("Priority", radioPriorityButton.getText().toString());

            selectedId = radioStatusGroup.getCheckedRadioButtonId();
            RadioButton radioStatusButton = (RadioButton) findViewById(selectedId);
            data.putExtra("Status", radioStatusButton.getText().toString());
            setResult(RESULT_OK, data);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (EditItemActivity)getActivity(), year, month, day);
        }


    }
}
