package com.cnpeak.expressreader.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cnpeak.expressreader.R;

/**
 * 自定义带tag标签的文字显示控件，项目只需要用到开头的tag标签
 */
public class TagText extends AppCompatTextView {
    private Context context;

    /**
     * 针对tag文字样式的相关属性
     */
    @ColorInt
    private int mTagTextColor;//tab文本颜色
    private float mTagTextSize;//tag文本大小
    private Drawable mTagBackgroundDrawable;//tag的文本背景素材


    public TagText(Context context) {
        super(context);
    }

    public TagText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    /**
     * 初始化Tag相关属性
     */
    private void init(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagText);
        mTagTextColor = typedArray.getColor(R.styleable.TagText_tagTextColor,
                context.getResources().getColor(R.color.expressreader_white));
        mTagBackgroundDrawable = typedArray.getDrawable(R.styleable.TagText_tagDrawable);
        mTagTextSize = typedArray.getDimensionPixelSize(R.styleable.TagText_tagTextSize, 11);
        typedArray.recycle();
    }


    /**
     * 设置开头的tag样式
     */
    public void setLeftTag(String tag, String content) {
        if (!TextUtils.isEmpty(tag)) {
            //tag标签文字不为空则进行相关的操作
            StringBuffer buffer = new StringBuffer();
            buffer.append(tag);
            buffer.append("\t");//添加空格
            buffer.append(content);
            //1、添加内容,加入可编辑状态对象
            SpannableString spannable = new SpannableString(buffer);
            //2、将tag文字转化成ImageSpan
            View view = LayoutInflater.from(context).inflate(R.layout.expressreader_include_tag_text, null);
            TextView tagText = view.findViewById(R.id.tv_expressreader_tag);
            tagText.setText(tag);
            tagText.setTextSize(mTagTextSize);
            tagText.setTextColor(mTagTextColor);
            view.setBackground(mTagBackgroundDrawable);
            Bitmap bitmap = convertViewToBitmap(view);
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            d.setBounds(0, 0, tagText.getWidth(), tagText.getHeight());//缺少这句的话，不会报错，但是图片不回显示
            CentreSpan span = new CentreSpan(d, ImageSpan.ALIGN_BOTTOM);
            spannable.setSpan(span, 0, tag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(spannable);
            setGravity(Gravity.CENTER_VERTICAL);
        } else {
            //tag标签文字为空，不进行添加标签的操作
            //3、设置当前Text文本的样式
            setText(content);
            setGravity(Gravity.CENTER_VERTICAL);
        }
    }


    /**
     * 设置结尾的tag样式
     */
    public void setRightTag(String tag, String content) {
        if (!TextUtils.isEmpty(tag)) {
            //tag标签文字不为空则进行相关的操作
            StringBuffer buffer = new StringBuffer();
            buffer.append(content);
            buffer.append("\t");//添加空格
            buffer.append(tag);
            //1、添加内容,加入可编辑状态对象
            SpannableString spannable = new SpannableString(buffer);
            //2、将tag文字转化成ImageSpan
            View view = LayoutInflater.from(context).inflate(R.layout.expressreader_include_tag_text, null);
            TextView tagText = view.findViewById(R.id.tv_expressreader_tag);
            tagText.setText(tag);
            tagText.setTextSize(mTagTextSize);
            tagText.setTextColor(mTagTextColor);
            view.setBackground(mTagBackgroundDrawable);
            Bitmap bitmap = convertViewToBitmap(view);
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            d.setBounds(0, 0, tagText.getWidth(), tagText.getHeight());//缺少这句的话，不会报错，但是图片不回显示
            CentreSpan span = new CentreSpan(d, ImageSpan.ALIGN_BOTTOM);
            spannable.setSpan(span, buffer.length() - tag.length() - 1, buffer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(spannable);
            setGravity(Gravity.CENTER_VERTICAL);
        } else {
            //tag标签文字为空，不进行添加标签的操作
            //3、设置当前Text文本的样式
            setText(content);
            setGravity(Gravity.CENTER_VERTICAL);
        }
    }

    /**
     * 将View转化成Bitmap
     */
    private Bitmap convertViewToBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

}
