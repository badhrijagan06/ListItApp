package com.example.badhri.listit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.badhri.listit.adapters.CustomAdapter;
import com.example.badhri.listit.R;
import com.example.badhri.listit.models.TodoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<TodoItem> todoItems;
    CustomAdapter aTodoAdapter;
    ListView lvitems;
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private final int REQUEST_CODE = 10;
    private final int REQUEST_CODE_NEW = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        setTitle("");
        populateArrayItems();
        lvitems = (ListView) findViewById(R.id.lvitems);
        //lvitems.setBackgroundColor(Color.parseColor("#80CBC4"));
        //findViewById(R.id.).setBackgroundColor(Color.parseColor("#E8EAF6"));
        lvitems.setAdapter(aTodoAdapter);
        lvitems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem item = (TodoItem) parent.getItemAtPosition(position);
                deleteItem(item.getId());
                todoItems.remove(position);
                aTodoAdapter.notifyDataSetChanged();

                return false;
            }
        });
        lvitems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(parent.getContext(), EditItemActivity.class);
                TodoItem item = (TodoItem)parent.getItemAtPosition(position);
                i.putExtra("Item",item.item);
                i.putExtra("Position", position);
                i.putExtra("Id", item.getId());
                Log.i("Listit", "position" + position);
                i.putExtra("Comments", item.comments);
                i.putExtra("Priority", item.priority);
                i.putExtra("Status", item.status);
                i.putExtra("Deadline", item.date);
                Log.i("ListIt", "send id:" + item.getId() + " item:" + item);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        Toast.makeText(MainActivity.this, R.string.intro, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent i = new Intent(getApplicationContext(), EditItemActivity.class);
            startActivityForResult(i, REQUEST_CODE_NEW);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK ) {
            String itemtext = data.getStringExtra("Item");
            String comments = data.getStringExtra("Comments");
            String deadline = data.getStringExtra("Deadline");
            String priority = data.getStringExtra("Priority");
            String status = data.getStringExtra("Status");


            if (requestCode == REQUEST_CODE) {
                int pos = data.getIntExtra("Position", 0);
                Long id = data.getLongExtra("Id", 0);
                TodoItem current = todoItems.get(pos);
                current.item = itemtext;
                Log.d("Lisit", "item" + current.item + comments + priority + status + deadline);
                Log.i("ListIt", "receive id:" + id + " item:" + itemtext);
                current.comments = comments;
                current.priority = priority;
                current.status = status;
                current.date = deadline;
                aTodoAdapter.notifyDataSetChanged();
                modifyItem(id, itemtext, comments, deadline, priority, status);
            }
            if (requestCode == REQUEST_CODE_NEW) {

                TodoItem item = new TodoItem(itemtext, deadline, priority, comments, status);
                aTodoAdapter.add(item);
                item.save();
            }
            Toast.makeText(MainActivity.this, R.string.itemsaved, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, R.string.emptyitem, Toast.LENGTH_SHORT).show();
        }
    }



    public void populateArrayItems() {
        readItems();
        aTodoAdapter = new CustomAdapter(this, todoItems);


    }
    private void readItems() {
        todoItems = new ArrayList<TodoItem>();


        List<TodoItem> items = TodoItem.listAll(TodoItem.class);
            Iterator<TodoItem> iter = items.iterator();
            while (iter.hasNext()){
                todoItems.add(iter.next());
            }


    }

    private void deleteItem(Long id) {
        TodoItem cur = TodoItem.findById(TodoItem.class, id);
        cur.delete();
    }

    private void modifyItem(Long id, String item, String comment, String deadline, String prioirty, String status) {
        Log.i("ListIt", "modify id:" + id + " item:" + item);
        TodoItem cur = TodoItem.findById(TodoItem.class, id);
        cur.item = item;
        cur.comments = comment;
        cur.priority = prioirty;
        cur.status = status;
        cur.date = deadline;
        cur.save();
    }




}
