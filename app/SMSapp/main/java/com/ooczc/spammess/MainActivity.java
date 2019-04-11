package com.ooczc.spammess;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    Button bt;
    EditText et;
    List<Map<String,Object>> SMSlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.listview);


        ListView listView = (ListView) findViewById(R.id.lv_main);



        SMSlist = new ArrayList<Map<String,Object>>();

        getSmsFromPhone();


        Log.i("zcc","---------MainActivity  111");

//        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.item,
//                new String[]{"num","mess"},new int[]{R.id.tv_num,R.id.tv_mess}
//                );
        MyAdapter adapter = new MyAdapter(this);
        adapter.setList(SMSlist);

        listView.setAdapter(adapter);

//        listView.setOnItemClickListener(this);

//        listView.setOnItemLongClickListener(this);


        Log.i("zcc","---------MainActivity  222");

//       String[] data = {"11","22","33","44","(･ｪ-)"};
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this,android.R.layout.simple_list_item_1,data
//        );
//        listView.setAdapter(adapter);
        /*
        et = (EditText) findViewById(R.id.et_1);


        bt = (Button) findViewById(R.id.button1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et.getText().toString();
                Toast.makeText(MainActivity.this,text,Toast.LENGTH_LONG).show();


            }
        });


        TextView tv = (TextView) findViewById(R.id.tv_a1);
        //
        String text = (String) tv.getText();
        //输出日志
        Log.i("zc",text);
        //吐司消息
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
        tv.setText("eeeeeeeeeeee");
        // .java 里这么引用资源，和xml里不同。
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        */

    }
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    public void getSmsFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[] {"_id", "address", "person",
                "body", "date", "type" };//"_id", "address", "person",, "date", "type


        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");

        if (null == cur) {


            Map<String,Object> map = new HashMap<String,Object>();
            map.put("Sender","404");
            map.put("Message","Error Reading SMS！");
            SMSlist.add(map);
            return;
        }


        while(cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("Sender",number);
            map.put("Message",body);
            SMSlist.add(map);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent,
                            View view, int position, long id) {
//        Toast.makeText(this,"点击"+position,Toast.LENGTH_SHORT).show();
//
//        Intent intent = new Intent();
//        intent.setClass(this,MessActivity.class);
//
//        Map<String,Object> map =
//                (Map<String, Object>) parent.getItemAtPosition(position);
//        intent.putExtra("index",""+map.get("num"));
//        intent.putExtra("index2",""+map.get("mess"));
//        startActivity(intent);


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent,
                                   View view, int position, long id) {

//        Toast.makeText(this,"长按"+position,Toast.LENGTH_SHORT).show();

//        final AdapterView a = parent;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.people);
        builder.setTitle("个性化过滤");
        builder.setMessage("是否把当前短信发送至服务器，实现个性化过滤？");
        builder.setNegativeButton("不发送", null);
        builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,
                        "已发送", Toast.LENGTH_SHORT).show();

//                String str = (String)list.get(which).get("mess");

//                Log.i("zcc", "---------onItemLongClick  000 " + list.get(which).get("mess"));

//                Map<String, Object> map =
//                        (Map<String, Object>) a.getItemAtPosition(which);
//                intent.putExtra("index",""+position);
//                Log.i("zcc", "---------onItemLongClick  111 " + map.get("mess"));
//                map.get("mess");
            }
        });
        builder.create().show();

        return true; //false表示不消化事件，事件继续传递下去,传给点击事件
    }
}

/*
public void onClick(DialogInterface dialog, int which) {
                Toast.makeText (MainActivity.this,
                        "已发送",Toast.LENGTH_SHORT).show();
                Map<String,Object> map =
                        (Map<String, Object>) parent.getItemAtPosition(position);
//                intent.putExtra("index",""+position);
                map.get("mess");

 */