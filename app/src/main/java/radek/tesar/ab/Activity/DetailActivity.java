package radek.tesar.ab.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import radek.tesar.ab.Client.entity.Transaction;
import radek.tesar.ab.R;

/**
 * Created by tesar on 28.04.2017.
 */

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = DetailActivity.class.getSimpleName();
    private Transaction trans= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTrans((Transaction) getIntent().getSerializableExtra(TAG));
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
//            bar.setDefaultDisplayHomeAsUpEnabled(true);
//            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);

            bar.setTitle(R.string.detail_title);

        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Transaction getTrans() {
        return trans;
    }

    public void setTrans(Transaction trans) {
        if(trans != null) {
            this.trans = trans;
        }
    }
}

