package com.example.imm.citi.activities;

/**
 * Created by imm on 6/14/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.imm.citi.R;
import com.example.imm.citi.agents.Agent;
import com.example.imm.citi.technicalClasses.Database;
import com.example.imm.citi.technicalClasses.RetrievalData;
import com.example.imm.citi.technicalClasses.Service;
import com.example.imm.citi.technicalClasses.User;
import com.example.imm.citi.technicalClasses.VolleyCallback;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {
    private static final String SERVICEPOPPHP = "updateServicePopularity.php";
    String service;
    Button btnFilterConfirm;
    Service srv;
    LinearLayout ll;
    TextView txtCaption;
    private String district;
    Button search;
    private RecyclerView agentlistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        service = intent.getStringExtra("servicename");
        setTitle(service);

        district = intent.getStringExtra("chosenService");

        ll = (LinearLayout) findViewById(R.id.ll_filters);

        setFilters();
        setListeners();

      /*  FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frmTitleFilter, new TitleFragment());
        fragmentTransaction.commit();

        Intent intent = getIntent();
        service = intent.getStringExtra("service");
        chosenService = intent.getStringExtra("chosenService");

        txtCaption = (TextView) findViewById(R.id.txtFilterCaption);
        txtCaption.setText(txtCaption.getText().toString()+ " (" + service + ")");
        ll = (LinearLayout) findViewById(R.id.ll_filters);

        setFilters();
        setListeners();
*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public String getService(){
        return service;
    }

    private void setFilters() {
        srv = new Service(service, this, district);
        srv.fetchFilters();
    }


    private void setListeners() {
        btnFilterConfirm = (Button)findViewById(R.id.btnFilterConfirm);
        btnFilterConfirm.setEnabled(false);

        btnFilterConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.search(srv);

                updateRating();
            }
        });
    }

    private void updateRating() {
        ArrayList<String> keys = new ArrayList<>(), vals = new ArrayList<>();
        keys.add("service");
        vals.add(service);

        Database db = new Database();
        db.retrieve(new RetrievalData(keys, vals, SERVICEPOPPHP, this), false, new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
            }
        });
    }

    public void showResult(ArrayList<Agent> agents){
        System.out.println("AAAAARRRREEEHHHH\n" + agents);

        Intent intent = new Intent(FilterActivity.this,AgentListActivity.class);
        intent.putExtra("servicename",service);
        Bundle b = new Bundle();
        b.putParcelableArrayList("agents", agents);
        intent.putExtra("agent", b);
        startActivity(intent);

//        Intent intent = new Intent(this, AgentListActivity.class);
//        Bundle b = new Bundle();
//        b.putParcelableArrayList("agents", agents);
//        intent.putExtra("agent", b);
//        intent.putExtra("service", service);
//        startActivity(intent);
    }

    public void enableSearch() {
        btnFilterConfirm.setEnabled(true);
    }

    /*@Override
    protected void onRestart() {
        super.onRestart();
        ll.removeAllViews();
        srv = new Service(service, this, district);
        srv.fetchFilters();
    }
*/
/*    private void setFilters() {
        srv = new Service(service, this, chosenService);
        srv.fetchFilters();
    }

    public void showResult(ArrayList<Agent> agents){
        System.out.println("AAAAARRRREEEHHHH\n" + agents);
        Intent intent = new Intent(this, AgentListActivity.class);
        Bundle b = new Bundle();
        b.putParcelableArrayList("agents", agents);
        intent.putExtra("agent", b);
        intent.putExtra("service", service);
        startActivity(intent);
    }

    public void enableSearch() {
        btnFilterConfirm.setEnabled(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ll.removeAllViews();
        srv = new Service(service, this, chosenService);
        srv.fetchFilters();
    }*/
}
