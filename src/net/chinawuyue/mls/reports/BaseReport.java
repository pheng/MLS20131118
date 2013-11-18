package net.chinawuyue.mls.reports;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.chinawuyue.mls.MyHScrollView;
import net.chinawuyue.mls.MyHScrollView.OnScrollChangedListener;
import net.chinawuyue.mls.Constant;
import net.chinawuyue.mls.R;
import net.chinawuyue.mls.XListView;
import net.chinawuyue.mls.board.BoardReport;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * �������
 * 
 */
public abstract class BaseReport implements XListView.IXListViewListener,
		OnItemClickListener {

	// ��������
	public static enum ReportType {
		LoanBalance, BusinessSurvey, SubjectBalance, 
		LoanAnalysis1, LoanAnalysis2, LoanAnalysis3,
		LoanRate1, LoanRate2, LoanRate3,LoanRate4
	};

	// �����ֶ�
	public static final String CODENO = "CODENO";
	public static final String RETURNCODE = "RETURNCODE";
	public static final String ERRORCODE = "ERRORCODE";
	public static final String ERRORMSG = "ERRORMSG";
	public static final String USERID = "USERID";
	public static final String USERNAME = "USERNAME";
	public static final String ORGID = "ORGID";
	public static final String ORGNAME = "ORGNAME";
	public static final String REPORTNO = "REPORTNO";
	public static final String DATE = "DATE";
	public static final String UNITNAME = "UNITNAME";
	public static final String COUNT = "COUNT";
	public static final String BALANCE = "BALANCE";
	public static final String PROJECTNAME = "PROJECTNAME";
	public static final String SUPERORGID = "SUPERORGID";
	public static final String SUPERORGNAME = "SUPERORGNAME";
	public static final String BUSSINESSNAME = "BUSINESSNAME";
	public static final String VOUCHNAME = "VOUCHNAME";
	public static final String INDUSTRYNAME = "INDUSTRYNAME";
	public static final String SUM = "SUM";
	public static final String RATIO = "RATIO";
	public static final String SUBJECTNO = "SUBJECTNO";
	public static final String SUBJECTNAME = "SUBJECTNAME";
	public static final String BUSINESSTYPENAME = "BUSINESSTYPENAME";
	public static final String YESORNO = "YESORNO";
	public static final String QUERYORGID = "QUERYORGID";
	public static final String QUERYORGNAME = "QUERYORGNAME";
	public static final String BOARDNO = "BOARDNO";
	public static final String BOARDTITLE = "BOARDTITLE";
	public static final String BOARDNAME = "BOARDNAME";
	public static final String BOARDDESC = "BOARDDESC";
	public static final String ISNEW = "ISNEW";
	public static final String INPUTDATE = "INPUTDATE";

	public static final int DELAY = 200; // �ӳ�ʱ��
	private Context context; // ������
	protected List<Map<String, Object>> xItems = null;// �������ݼ���
	private View layout = null; // �����������ͼ
	protected XListView reportList = null; // ListView
	private Handler xHandler = null; // �̴߳���
	// protected HorizontalScrollView scrollView = null; //������ͼ
	protected ReportRequest request; // ������
	private ReportType reportType; // ��������
	private Class activityClass = ChartActivity.class;// ��ת������
	protected ViewGroup mHead; // ��ͷ
	protected boolean isScrollLeft = false;
	protected boolean isScrolled = false;
	protected boolean isNeedUpdate = false;
	public ColorStateList csl;

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public BaseReport(Context context) {
		this.context = context;
		Resources resource = (Resources) context.getResources();
		csl = (ColorStateList) resource
				.getColorStateList(R.color.list_title_color);
	}

	public void clear() {
		if (xItems != null)
			xItems.clear();
	}

	// �����ͼ
	public View getReportView() {
		fetchData();
		return layout;
	}

	// ����������
	public void setReportRequest(ReportRequest request) {
		this.request = request;
	}

	// ���ñ�����ͼ
	public void setReportView(int layoutId) {
		layout = LayoutInflater.from(context).inflate(layoutId, null);
		this.reportList = (XListView) layout.findViewById(R.id.listview_report);
		mHead = (ViewGroup) layout.findViewById(R.id.head);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		setTextColor(mHead);
		// �ж�����ǹ�������Ҫ�̶���ͷ
		if (!(this instanceof BoardReport)) {
			// �̶���ͷ
			mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
			reportList
					.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		}
		xHandler = new Handler();
		this.reportList.setOnItemClickListener(this);
		this.reportList.setXListViewListener(this);
	}

	private double downX,downY,upX,upY;
	
	/** ListView���������� */
	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			//�ж��Ƿ������
			if(arg1.getAction()==MotionEvent.ACTION_DOWN){
				downX = arg1.getX();
				downY = arg1.getY();
				isScrolled = false;
			}else if(arg1.getAction()==MotionEvent.ACTION_UP){
				upX = arg1.getX();
				upY = arg1.getY();
				if(Math.abs(downX-upX)>Constant.MAX_SCROLL_DISTANCE||
						Math.abs(downY-upY)>Constant.MAX_SCROLL_DISTANCE){
					isScrolled = true;
				}
			}
			// ������ͷ �� listView�ؼ���touchʱ�������touch���¼��ַ��� ScrollView
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

	// ���ñ�ͷ������ɫ
	private void setTextColor(ViewGroup viewGroup) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {
			View view = viewGroup.getChildAt(i);
			if (view instanceof TextView) {
				((TextView) view).setTextColor(csl);
			} else if (view instanceof ViewGroup) {
				setTextColor((ViewGroup) view);
			}
		}
	}

	private void onLoad() {
		this.reportList.stopRefresh();
		this.reportList.stopLoadMore();
	}

	@Override
	public void onRefresh() {
		xHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				xItems.clear();
				isNeedUpdate = true;
				fetchData();
				onLoad();
			}
		}, DELAY);
	}

	@Override
	public void onLoadMore() {
		xHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				isNeedUpdate = true;
				fetchData();
				onLoad();
			}
		}, DELAY);
	}

	// �������
	public abstract void fetchData();

	// ����������
	public void setAdapter(BaseAdapter adapter) {
		// ������������涨��������������ˢ��
		if (adapter.getCount() > Integer.parseInt(context
				.getString(R.string.xlistview_report_limit)))
			this.reportList.setPullLoadEnable(true);
		else
			this.reportList.setPullLoadEnable(false);
		this.reportList.setAdapter(adapter);
	}

	public ListAdapter getAdapter() {
		return reportList.getAdapter();
	}

	/**
	 * ������ת��Actiivty
	 * 
	 * @param activityClass
	 */
	public void setActivityClass(Class activityClass) {
		this.activityClass = activityClass;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		// û�л���������µ��
		if (!isScrolled) {
			intentTo(pos);
		}
	}

	// ��ת����������
	private void intentTo(int pos) {
		Intent intent = new Intent(context, activityClass);
		Bundle bun = new Bundle();
		// ��ֵ �������ݺͱ�������
		if(xItems == null || xItems.size() < 1){
			return;
		}
		bun.putSerializable("map", (Serializable) xItems.get(pos - 1));
		bun.putSerializable("reportType", reportType);
		intent.putExtras(bun);
		context.startActivity(intent);
	}

	// ScrollView�Ƿ�����������
	public boolean isScorllLeft() {
		return isScrollLeft;
	}

	/** ���ƻ�����ͼ�ļ����� */
	class OnScrollChangedListenerImp implements OnScrollChangedListener {
		MyHScrollView mScrollViewArg;

		public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
			mScrollViewArg = scrollViewar;
		}

		@Override
		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			// ������ͼ
			mScrollViewArg.smoothScrollTo(l, t);
			// �����Ƿ񻬶��������
			isScrollLeft = l <= 0;
		}
	};

}
