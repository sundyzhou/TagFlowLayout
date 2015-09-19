/**
 * 
 */
package com.example.tagflowlayout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 黄艳武
 */
public class TagFlowLayout extends ViewGroup {

	/** 是否分配剩余空间 */
	private boolean IsAssaginSpace = false;

	private SingleLine currentLine;

	/**
	 * 列,行间距
	 */
	private int mVetivalSpace, mHorizontalSpace;

	/**
	 * 用来存放每一行的集合
	 **/
	private List<SingleLine> mLines = new ArrayList<TagFlowLayout.SingleLine>();

	/** 使用的长度 **/
	private int useWidth;

	/** 父控件的总高度 */
	private int totalHeight;

	private int mParentWidth;

	public TagFlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mVetivalSpace = DisplayUtil.dip2px(context, 5);
		mHorizontalSpace = DisplayUtil.dip2px(context, 5);

		TypedArray a = null;
		try {
			a = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
			getPeroperties(context, a);
		} finally {
			a.recycle();
		}

	}

	/**
	 * 获取在布局文件中的属性
	 * 
	 * @param context Context
	 * @param a TypeArray
	 */
	private void getPeroperties(Context context, TypedArray a) {
		int count = a.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
				case R.styleable.TagFlowLayout_isAssaginSpace:
					IsAssaginSpace = a.getBoolean(attr, IsAssaginSpace);
					break;
				default:
					break;
			}
		}
	}

	public TagFlowLayout(Context context) {
		super(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		useWidth = 0;
		currentLine = null;
		mLines.clear();
		// // 先拿到父控件的测量模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		// 通过测量规则测得长宽,再减去padding部分,得到父控价可用的空间
		mParentWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int mParentHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

		// 之前没有確定view的測量模式,制定测量规则,导致测出来的子View大小都一样
		int childeWidthMode = widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode;
		int childeHeightMode = heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode;
		// 指定子控件的测量规则
		int ChildMeasureWithSpec = MeasureSpec.makeMeasureSpec(childeWidthMode, mParentWidth);
		int ChildMeasureHeightSpec = MeasureSpec.makeMeasureSpec(childeHeightMode, mParentHeight);

		ParentMeasureDimension(heightMeasureSpec, mParentWidth, ChildMeasureWithSpec, ChildMeasureHeightSpec);
	}

	/**
	 * 重新确定父控件的宽,高
	 */
	private void ParentMeasureDimension(int heightMeasureSpec, int mParentWidth, int ChildMeasureWithSpec, int ChildMeasureHeightSpec) {
		MeasrueChild(mParentWidth, ChildMeasureWithSpec, ChildMeasureHeightSpec);

		totalHeight = mVetivalSpace * (mLines.size() - 1) + currentLine.LineHeight * mLines.size() + getPaddingBottom() + getPaddingTop();

		// 需要让父控件设置长宽;在上面测量出来的宽度能确定,resovle:决定
		setMeasuredDimension(mParentWidth + getPaddingLeft() + getPaddingRight(), resolveSize(totalHeight, heightMeasureSpec));
	}

	/**
	 * 测量子View并添加到行中
	 * 
	 * @param mParentWidth 父控件的宽度
	 * @param ChildMeasureWithSpec 子View的宽测量规则
	 * @param ChildMeasureHeightSpec 子View的高测量规则
	 */
	private void MeasrueChild(int mParentWidth, int ChildMeasureWithSpec, int ChildMeasureHeightSpec) {

		int mChildCount = getChildCount();
		currentLine = new SingleLine();
		for (int i = 0; i < mChildCount; i++) {
			View view = getChildAt(i);
			// 然后还要获得子控件的长宽高,通过测量规则,直接测量出来
			view.measure(ChildMeasureWithSpec, ChildMeasureHeightSpec);
			int mChildWidth = view.getMeasuredWidth();
			int mChildHeight = view.getMeasuredHeight();
			useWidth = mChildWidth + mHorizontalSpace / 2 + useWidth;
			if (useWidth <= mParentWidth) {
				currentLine.LineHeight = mChildHeight;
				currentLine.addToLine(view);
			} else {
				newLine(view);
			}
		}
	}

	/**
	 * 如果宽度不够,就重新添加到另外一条线上去
	 */
	private void newLine(View view) {
		mLines.add(currentLine);
		currentLine = new SingleLine();
		// 之前判断玩之后,没有把上一个不能存放的View放进新的线中
		currentLine.addToLine(view);
		useWidth = 0;
		useWidth += view.getMeasuredWidth();
	}

	/**
	 * @param isAssaginSpace the isAssaginSpace to set
	 */
	public void setIsAssaginSpace(boolean isAssaginSpace) {
		this.IsAssaginSpace = isAssaginSpace;
		postInvalidate();
	}

	/** 对控件重新布局 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		l += getPaddingLeft();
		t += getPaddingTop();
		for (SingleLine line : mLines) {
			line.ChildLayout(l, t);
			t = t + line.getLineHeight() + mVetivalSpace;
		}
	}

	/** 行对象 **/
	class SingleLine {

		public int LineHeight = 0;

		int LineWidth = 0;

		ArrayList<View> ViewList = new ArrayList<View>();

		public void addToLine(View tView) {
			if (LineHeight < tView.getMeasuredHeight()) {
				LineHeight = tView.getMeasuredHeight();
			}
			LineWidth += tView.getMeasuredWidth();
			ViewList.add(tView);
		};

		/**
		 * @param l 确定每个View的位置
		 * @param t
		 */
		public void ChildLayout(int l, int t) {

			LineWidth += mHorizontalSpace * (ViewList.size() - 1);
			// 进行多余空间的分配
			int SurplusSpace = mParentWidth - LineWidth;
			int sur = 0;
			if (IsAssaginSpace) {
				sur = SurplusSpace / ViewList.size();
			}

			for (int i = 0; i < ViewList.size(); i++) {
				View view = ViewList.get(i);
				view.layout(l, t, l + view.getMeasuredWidth() + sur, t + view.getMeasuredHeight());
				l += mHorizontalSpace + view.getMeasuredWidth() + sur;

				// 在这里设置重新设置控件的padding,使文字居于中间,在此之前,随着view的宽度变大,文字是不居于中间的:哈哈
				view.setPadding(view.getPaddingLeft() + sur / 2, view.getPaddingTop(), view.getPaddingRight() + sur / 2, view.getPaddingBottom());
			}
		}

		public ArrayList<View> getViewList() {
			return ViewList;
		}

		/**
		 * @return the lineHeight
		 */
		public int getLineHeight() {
			return LineHeight;
		}

		/**
		 * @return the lineWidth
		 */
		public int getLineWidth() {
			return LineWidth;
		}
	}
}
