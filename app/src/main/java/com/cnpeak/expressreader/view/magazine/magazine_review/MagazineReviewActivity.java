package com.cnpeak.expressreader.view.magazine.magazine_review;

import android.content.Intent;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cnpeak.expressreader.R;
import com.cnpeak.expressreader.base.BaseActivity;
import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.base.IView;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.model.bean.IssueList;
import com.cnpeak.expressreader.model.bean.MagazineList;

import java.io.Serializable;
import java.util.List;

/**
 * 往期杂志封面展示页
 */
public class MagazineReviewActivity extends BaseActivity<IView, BasePresenter<IView>>
        implements IView, MagazineReviewAdapter.OnIssueItemSelectListener {
    //数据源
    private List<IssueList> listBeanList;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.expressreader_activity_magazine_review;
    }

    @Override
    protected void getExtraFromBundle(Intent intent) {
        super.getExtraFromBundle(intent);
        Serializable serializableExtra = intent.getSerializableExtra(ErConstant.ISSUE_LIST);
        if (serializableExtra instanceof MagazineList) {
            MagazineList magazineList = (MagazineList) serializableExtra;
            listBeanList = magazineList.getIssueList();
        }
    }

    @Override
    protected MagazineReviewPresenter initPresenter() {
        return new MagazineReviewPresenter(this);
    }

    @Override
    public void initView() {
        //初始化关闭按钮
        initAction();
        //数据展示容器
        initRecyclerView();
    }

    private void initAction() {
        ImageView mIvExit = findViewById(R.id.iv_fragment_issue_close);
        mIvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView mRvIssueList = findViewById(R.id.rv_fragment_issue_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false);
        mRvIssueList.setLayoutManager(layoutManager);
        mRvIssueList.setHasFixedSize(true);

        mRvIssueList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                //第一条距离顶部的距离
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                if (childAdapterPosition == 0) {
                    outRect.top = 130;
                } else {
                    outRect.top = 30;
                }
            }
        });

        if (listBeanList != null) {
            //初始化适配器相关
            MagazineReviewAdapter mIssueListAdapter = new MagazineReviewAdapter(
                    this, listBeanList);
            mIssueListAdapter.setOnIssueItemSelectListener(this);
            mRvIssueList.setAdapter(mIssueListAdapter);
        }
    }

    @Override
    public void onIssueItemSelected(String issueId, int position) {
        Intent intent = new Intent();
        intent.putExtra(ErConstant.ISSUE_ID, issueId);
        intent.putExtra(ErConstant.ISSUE_INDEX, position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
