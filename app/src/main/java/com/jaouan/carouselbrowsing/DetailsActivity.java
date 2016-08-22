package com.jaouan.carouselbrowsing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.jaouan.carouselbrowsing.models.Item;

/**
 * Details activity.
 */
public class DetailsActivity extends AppCompatActivity {

    /**
     * Extra - Item.
     */
    public static final String EXTRA_ITEM = "item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // - Bind datas.
        final Item item = (Item) getIntent().getSerializableExtra(EXTRA_ITEM);
        ((ImageView) findViewById(R.id.picture)).setImageResource(item.getImage());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }

}
