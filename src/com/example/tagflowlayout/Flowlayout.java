package com.example.tagflowlayout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class Flowlayout extends ViewGroup {

	private int horizontolSpacing;

	private int verticalSpacing;

	public Flowlayout(Context context) {
		super(context);
	}

	public Flowlayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private Line currentline;// ��ǰ����

	private int useWidth = 0;// ��ǰ��ʹ�õĿ��

	private List<Line> mLines = new ArrayList<Flowlayout.Line>();

	private int width;

	public Flowlayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		horizontolSpacing = DisplayUtil.dip2px(context, 15);
		verticalSpacing = DisplayUtil.dip2px(context, 15);
	}

	// ���� ��ǰ�ؼ�Flowlayout
	// ���������������ÿ�����ӵ�
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		// MeasureSpec.EXACTLY;
		// MeasureSpec.AT_MOST;
		// MeasureSpec.UNSPECIFIED;

		mLines.clear();
		currentline = null;
		useWidth = 0;
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec); // ��ȡ��ǰ������(Flowlayout)��ģʽ
		width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingTop(); // ��ȡ����͸�

		int childeWidthMode;
		int childeHeightMode;
		// Ϊ�˲���ÿ������ ��Ҫָ��ÿ�����Ӳ�������
		childeWidthMode = widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode;
		childeHeightMode = heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode;

		int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childeWidthMode, width);
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childeHeightMode, height);

		currentline = new Line();// �����˵�һ��
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			System.out.println("���ӵ�����:" + getChildCount());
			// ����ÿ������
			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

			int measuredWidth = child.getMeasuredWidth();
			useWidth += measuredWidth;// �õ�ǰ�м���ʹ�õĳ���
			if (useWidth <= width) {
				currentline.addChild(child);// ��ʱ��֤����ǰ�ĺ����ǿ��ԷŽ�ǰ������,�Ž�ȥ
				useWidth += horizontolSpacing;
				if (useWidth > width) {
					// ����
					newLine();
				}
			} else {
				// ����
				if (currentline.getChildCount() < 1) {
					currentline.addChild(child); // ��֤��ǰ������������һ������
				}
				newLine();
			}

		}
		if (!mLines.contains(currentline)) {
			mLines.add(currentline);// ������һ��
		}
		int totalheight = 0;
		for (Line line : mLines) {
			totalheight += line.getHeight();
		}
		totalheight += verticalSpacing * (mLines.size() - 1) + getPaddingTop() + getPaddingBottom();

		System.out.println(totalheight);
		setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), resolveSize(totalheight, heightMeasureSpec));
	}

	private void newLine() {
		mLines.add(currentline);// ��¼֮ǰ����
		currentline = new Line(); // �����µ�һ��
		useWidth = 0;
	}

	private class Line {

		int height = 0; // ��ǰ�еĸ߶�

		int lineWidth = 0;

		private List<View> children = new ArrayList<View>();

		/**
		 * ���һ������
		 * 
		 * @param child
		 */
		public void addChild(View child) {
			children.add(child);
			if (child.getMeasuredHeight() > height) {
				height = child.getMeasuredHeight();
			}
			lineWidth += child.getMeasuredWidth();
		}

		public int getHeight() {
			return height;
		}

		/**
		 * ���غ��ӵ�����
		 * 
		 * @return
		 */
		public int getChildCount() {
			return children.size();
		}

		public void layout(int l, int t) {
			lineWidth += horizontolSpacing * (children.size() - 1);
			int surplusChild = 0;
			int surplus = width - lineWidth;
			if (surplus > 0) {
				surplusChild = surplus / children.size();
			}
			for (int i = 0; i < children.size(); i++) {
				View child = children.get(i);
				// getMeasuredWidth() �ؼ�ʵ�ʵĴ�С
				// getWidth() �ؼ���ʾ�Ĵ�С
				child.layout(l, t, l + child.getMeasuredWidth() + surplusChild, t + child.getMeasuredHeight());
				l += child.getMeasuredWidth() + surplusChild;
				l += horizontolSpacing;
			}
		}

	}

	// ����ÿ�����ӵ�λ��
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		l += getPaddingLeft();
		t += getPaddingTop();
		for (int i = 0; i < mLines.size(); i++) {
			Line line = mLines.get(i);
			line.layout(l, t); // ����ÿһ��ȥ����
			t += line.getHeight() + verticalSpacing;
		}
	}

}
