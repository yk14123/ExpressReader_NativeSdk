package com.cnpeak.expressreader.view.dialog;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.model.bean.Category;
import com.cnpeak.expressreader.view.news.NewsCategoryAdapter;

import java.util.Collections;
import java.util.List;

/**
 * 热点资讯标签顺序编辑对话框
 *
 * @author HUANG_JIN
 * builder at 18/12/28
 */
public class NewsTabSortWindow extends PopupWindow implements PopupWindow.OnDismissListener {
    private static final String TAG = "NewsTabSortWindow";
    //上下文
    private Context context;
    //编辑按钮（拖拽操作）
    private TextView mTvChannelEdit;
    //列表控件
    private RecyclerView mRvChannelList;
    //频道列表数据
    private List<Category> mCategories;
    //适配器
    private NewsCategoryAdapter mNewsCategoryAdapter;
    //当前是否可以编辑
    private boolean isChannelEditable = false;
    //当前选中项
    private int mCurrentSelectedIndex = 0;
    //回调事件
    private OnNewsSelectedListener onNewsSelectedListener;

    public NewsTabSortWindow(Context context) {
        this(context, null);
    }

    private NewsTabSortWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private NewsTabSortWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        //内容
        View mContentView = LayoutInflater.from(context).inflate(
                R.layout.expressreader_dialog_category_sort, null);
        //关闭按钮
        ImageView mIvClose = mContentView.findViewById(R.id.iv_category_window_close);
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Tab列表
        mRvChannelList = mContentView.findViewById(R.id.rv_category_tabs);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        mRvChannelList.setLayoutManager(manager);
        mRvChannelList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 15;
            }
        });
        //添加Item拖拽的工具
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            //设置可以对ViewHolder做什么操作
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder viewHolder) {
                Log.d(TAG, "getMovementFlags >>>");
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = 0;
                //获取当前viewHolder的position位置
                int adapterPosition = viewHolder.getAdapterPosition();
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                //如果当前是第一个item的话，不做任何操作
                if (adapter != null && adapterPosition == swipeFlags) {
                    return swipeFlags;
                }
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            //长按item的时候就会进入拖拽并在拖拽过程中不断回调
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                Log.d(TAG, "onMove >>>");
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                //如果当前位置为0，直接返回
                if (toPosition == 0) {
                    return false;
                } else if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mCategories, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(mCategories, i, i - 1);
                    }
                }
                mNewsCategoryAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            //我们滑动item的时候就会调用
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Log.d(TAG, "onSwiped >>>");
            }

            //指定可以支持拖拽的item
            @Override
            public boolean isLongPressDragEnabled() {
                Log.d(TAG, "isLongPressDragEnabled >>>");
                return isChannelEditable;
            }

            //是否支持测滑
            @Override
            public boolean isItemViewSwipeEnabled() {
                return false;
            }
        });
        mItemTouchHelper.attachToRecyclerView(mRvChannelList);

        //提示信息
        final TextView tvTips = mContentView.findViewById(R.id.tv_channel_tips);
        mTvChannelEdit = mContentView.findViewById(R.id.tv_channel_edit);
        mTvChannelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChannelEditable = !isChannelEditable;
                if (isChannelEditable) {
                    mTvChannelEdit.setText(context.getText(R.string.expressreader_done));
                    tvTips.setText(context.getText(R.string.expressreader_channel_drag_tips));
                    mNewsCategoryAdapter.seCurrentSelected(-1);
                } else {
                    mTvChannelEdit.setText(context.getText(R.string.expressreader_edit));
                    tvTips.setText(context.getText(R.string.expressreader_channel_tap_tips));
                    mNewsCategoryAdapter.seCurrentSelected(mCurrentSelectedIndex);
                }
            }
        });

        //添加内容视图
        setContentView(mContentView);
        //设置宽高布局参数
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置进场动画
        setAnimationStyle(R.style.ActionSheetDialogAnimation);
        //设置点击外部可以消失
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0));
        //设置消失监听
        setOnDismissListener(this);
    }

    /**
     * 设置频道数据
     */
    public void setCategories(List<Category> categories, int selectedIndex) {
        this.mCategories = categories;
        this.mCurrentSelectedIndex = selectedIndex;
        mNewsCategoryAdapter = new NewsCategoryAdapter(mCategories, mCurrentSelectedIndex);
        mRvChannelList.setAdapter(mNewsCategoryAdapter);
    }

    /**
     * 消失回调
     */
    @Override
    public void onDismiss() {
        Log.d(TAG, "onDismiss: >>>");
        isChannelEditable = false;
        if (onNewsSelectedListener != null) {
            onNewsSelectedListener.onNewsCategoryChanged(mCategories);
        }
    }

    /**
     * 点选事件
     */
    public interface OnNewsSelectedListener {
        /**
         * 当前选中项
         */
        void onNewsSelected(int position);

        /**
         * 当前列表拖拽顺序改变
         */
        void onNewsCategoryChanged(List<Category> categories);
    }

    public void setOnNewsSelectedListener(OnNewsSelectedListener onNewsSelectedListener) {
        this.onNewsSelectedListener = onNewsSelectedListener;
        mNewsCategoryAdapter.setOnNewsSelectedListener(onNewsSelectedListener);
    }

    /**
     * 设置当前选中项
     */
    public void setSelectedIndex(int selectedIndex) {
        this.mCurrentSelectedIndex = selectedIndex;
        mNewsCategoryAdapter.seCurrentSelected(selectedIndex);
    }

}