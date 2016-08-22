package com.jaouan.carouselbrowsing;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.jaouan.carouselbrowsing.models.Item;
import com.jaouan.carouselbrowsing.views.adapters.ItemAdapter;

/**
 * Carousel activity.
 */
public class CarouselActivity extends AppCompatActivity {

    /**
     * Items stubs.
     */
    private static final Item[] ITEMS = {
            new Item(R.drawable.pic_1),
            new Item(R.drawable.pic_2),
            new Item(R.drawable.pic_3),
            new Item(R.drawable.pic_1),
            new Item(R.drawable.pic_2),
            new Item(R.drawable.pic_3),
            new Item(R.drawable.pic_1),
            new Item(R.drawable.pic_2),
            new Item(R.drawable.pic_3)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);
        getSupportActionBar().setElevation(0);

        // - Initialize carousel.
        final RecyclerView carouselRecyclerView = (RecyclerView) findViewById(R.id.carousel);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        carouselRecyclerView.setLayoutManager(linearLayoutManager);
        new LinearSnapHelper().attachToRecyclerView(carouselRecyclerView);
        final ItemAdapter itemAdapter = new ItemAdapter(this, ITEMS);
        carouselRecyclerView.setAdapter(itemAdapter);

        // - Center recycler view first item.
        itemAdapter.setOnItemSizeListener(new ItemAdapter.OnItemSizeListener() {
            @Override
            public void onItemSize(final int size) {
                itemAdapter.setOnItemSizeListener(null);
                final int padding = (carouselRecyclerView.getMeasuredWidth() - size) / 2;
                carouselRecyclerView.setPadding(padding, 0, padding, 0);
                carouselRecyclerView.smoothScrollToPosition(0);
            }
        });

        // - On item click...
        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int position, final ItemAdapter.ViewHolder viewHolder) {

                // - If the clicked item is not centered, scroll to it.
                final int currentPosition = (linearLayoutManager.findFirstVisibleItemPosition() + linearLayoutManager.findLastVisibleItemPosition()) / 2;
                if (currentPosition != position) {
                    carouselRecyclerView.smoothScrollToPosition(position);
                }

                // - Else if collapsed, expand it.
                else if (itemAdapter.isCollapsed(position)) {
                    itemAdapter.expandItem(position, viewHolder);
                }

                // - Else (item centered and expanded), navigate to details.
                else {
                    final ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(CarouselActivity.this, viewHolder.picture, viewHolder.picture.getTransitionName());
                    final Intent intent = new Intent(CarouselActivity.this, DetailsActivity.class);
                    intent.putExtra(DetailsActivity.EXTRA_ITEM, ITEMS[position]);
                    startActivity(intent, options.toBundle());
                }

            }
        });
    }

}
