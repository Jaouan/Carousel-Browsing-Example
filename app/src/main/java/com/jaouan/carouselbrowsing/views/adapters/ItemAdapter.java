package com.jaouan.carouselbrowsing.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jaouan.carouselbrowsing.R;
import com.jaouan.carouselbrowsing.animations.interpolators.ReverseInterpolator;
import com.jaouan.carouselbrowsing.models.Item;

/**
 * Item adapter.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    /**
     * Value for no item expanded.
     */
    public static final int NONE = -1;

    /**
     * Context.
     */
    private final Context context;

    /**
     * Items.
     */
    private final Item[] items;

    /**
     * On item size listener.
     */
    private OnItemSizeListener onItemSizeListener;

    /**
     * On item click listener.
     */
    private OnItemClickListener onItemClickListener;

    /**
     * Current expanded item.
     */
    private int currentExpandedItem = -1;

    /**
     * Current expanded view holder.
     */
    private ViewHolder currentExpandedViewHolder;

    /**
     * Item adapter's constructor.
     *
     * @param context Context.
     * @param items   Items to display.
     */
    public ItemAdapter(@NonNull final Context context, @NonNull final Item[] items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (onItemSizeListener != null) {
                    onItemSizeListener.onItemSize(view.getMeasuredWidth());
                }
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        // - If holder is reuse by an other position, invalidate current expanded view holder.
        if (currentExpandedViewHolder == viewHolder) {
            currentExpandedViewHolder = null;
        }

        // - If user returns to expanded item, update current expanded view holder.
        if (currentExpandedItem == position) {
            currentExpandedViewHolder = viewHolder;
        }

        // - Bind datas.
        viewHolder.picture.setImageResource(items[position].getImage());

        // - Bind events.
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position, viewHolder);
                }
            }
        });

        // - Expand/collapse if necessary.
        if (currentExpandedItem == position) {
            expandView(viewHolder, false);
        } else {
            collapseView(viewHolder, false);
        }
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    /**
     * Defines on item size listener.
     *
     * @param onItemSizeListener On item size listener.
     */
    public void setOnItemSizeListener(final OnItemSizeListener onItemSizeListener) {
        this.onItemSizeListener = onItemSizeListener;
    }

    /**
     * Defines on item click listener.
     *
     * @param onItemClickListener On click listener.
     */
    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Expands item.
     *
     * @param position   Item's position.
     * @param viewHolder Item's view holder.
     */
    public void expandItem(final int position, final ViewHolder viewHolder) {
        if (currentExpandedItem != NONE && currentExpandedViewHolder != null) {
            collapseItem(currentExpandedViewHolder);
        }
        currentExpandedItem = position;
        currentExpandedViewHolder = viewHolder;

        expandView(viewHolder, true);
    }

    /**
     * Expands item's view.
     *
     * @param viewHolder Item's view holder.
     * @param animate    Animate.
     */
    private void expandView(final ViewHolder viewHolder, final boolean animate) {
        if (viewHolder.moreLayout.getVisibility() != View.VISIBLE) {
            viewHolder.moreLayout.setVisibility(View.VISIBLE);
            final Animation scaleMoreAnimation = AnimationUtils.loadAnimation(context, R.anim.reveal_more);
            if (!animate) {
                scaleMoreAnimation.setDuration(0);
            }
            viewHolder.moreLayout.startAnimation(scaleMoreAnimation);
            viewHolder.pictureLayout.animate()
                    .translationY(-context.getResources().getDimensionPixelSize(R.dimen.picture_translate_distance))
                    .setDuration(scaleMoreAnimation.getDuration())
                    .setInterpolator(scaleMoreAnimation.getInterpolator())
                    .start();
        }
    }

    /**
     * Collapses item.
     *
     * @param viewHolder Item's view holder.
     */
    public void collapseItem(final ViewHolder viewHolder) {
        currentExpandedItem = -1;
        collapseView(viewHolder, true);
    }

    /**
     * Collapse item's view.
     *
     * @param viewHolder Item's view holder.
     * @param animate    Animate.
     */
    private void collapseView(final ViewHolder viewHolder, final boolean animate) {
        if (viewHolder.moreLayout.getVisibility() != View.INVISIBLE) {
            viewHolder.moreLayout.setVisibility(View.INVISIBLE);
            final Animation scaleMoreAnimation = AnimationUtils.loadAnimation(context, R.anim.reveal_more);
            if (!animate) {
                scaleMoreAnimation.setDuration(0);
            }
            viewHolder.pictureLayout.animate()
                    .translationY(0)
                    .setDuration(scaleMoreAnimation.getDuration())
                    .setInterpolator(scaleMoreAnimation.getInterpolator())
                    .start();
            scaleMoreAnimation.setInterpolator(new ReverseInterpolator(scaleMoreAnimation.getInterpolator()));
            viewHolder.moreLayout.startAnimation(scaleMoreAnimation);
        }
    }

    /**
     * Determines if an item is collapsed.
     *
     * @param position Item position.
     * @return TRUE if collapsed.
     */
    public boolean isCollapsed(final int position) {
        return currentExpandedItem != position;
    }

    /**
     * View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View rootView;
        public final ImageView picture;
        public final View moreLayout;
        public final View pictureLayout;

        public ViewHolder(final View itemView) {
            super(itemView);
            rootView = itemView;
            picture = (ImageView) itemView.findViewById(R.id.picture);
            moreLayout = itemView.findViewById(R.id.more_layout);
            pictureLayout = itemView.findViewById(R.id.picture_layout);
        }

    }

    /**
     * On item size listener interface.
     */
    public interface OnItemSizeListener {
        void onItemSize(int size);
    }

    /**
     * On item click listener interface.
     */
    public interface OnItemClickListener {
        void onClick(int position, ItemAdapter.ViewHolder viewHolder);
    }

}
