package net.chinawuyue.mls.reports;

import java.util.Map;

import net.chinawuyue.mls.R;
import net.chinawuyue.mls.reports.AbstractChart.ChartType;
import net.chinawuyue.mls.reports.BaseReport.ReportType;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;


/**
 * ͼ�����
 * @author hp
 *
 */
public class ChartActivity extends SherlockActivity {

	private static final int ITEM1 = 1;
	private static final int ITEM2 = 2;
	private static final int ITEM3 = 3;
	private static final int ITEM4 = 4;
	private static final int ITEM5 = 5;
	private static final int ITEM6 = 6;
	private static final int ITEM7 = 7;
	private static final int ITEM8 = 8;
	private static final int ITEM9 = 9;
	private Map<String, Object> map;
	private ReportType reportType;
	private LinearLayout layout;
	private String balance;
	private String count;
	private String sum;
	private String ratio;
	private String chartType;
	private ChartType typeOfChart = ChartType.BARCHART;
	private String content;
	public static String unitName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
		//������Ļ�ֱ��ʼ���ͼ�������С
		Display dis = this.getWindowManager().getDefaultDisplay();
		int width = dis.getWidth();
		int height = dis.getHeight();
		int w = width > height ? width : height;
		int rate = (int)(5*((float)w / 200));
		System.out.println("font:"+rate);
		rate = rate < 18 ? 18 :rate;
		AbstractChart.textSize = rate;
		//��ȡ����
		balance = this.getResources().getString(R.string.balance);
		count = this.getResources().getString(R.string.count);
		sum = this.getResources().getString(R.string.sum);
		ratio = this.getResources().getString(R.string.ratio);
		chartType = this.getResources().getString(R.string.chart_type);
		content = balance;//Ĭ����ʾ���
		//��ñ�����洫�������ݺͱ�������
		map = (Map<String, Object>) getIntent().getExtras().get("map");
//		unitName = map.get(BaseReport.UNITNAME).toString();
		reportType = (ReportType) getIntent().getExtras().get("reportType");
		layout = (LinearLayout) this.findViewById(R.id.LinearLayout01);
		showChart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu subMenu1 = menu.addSubMenu("");
		//���ݱ���ͬ��Ӳ�ͬ�Ĳ˵�
		subMenu1.add(0, ITEM1, 0, count);
		subMenu1.add(0, ITEM2, 0, balance);
		if (reportType != ReportType.LoanBalance
				&& reportType != ReportType.BusinessSurvey
				&& reportType != ReportType.SubjectBalance
				&& reportType != ReportType.LoanRate1
				&& reportType != ReportType.LoanRate2
				&& reportType != ReportType.LoanRate3
				&& reportType != ReportType.LoanRate4) {
			subMenu1.add(0, ITEM3, 0, sum);
		}
		if (reportType != ReportType.LoanBalance
				&& reportType != ReportType.SubjectBalance) {
			String strRatio = ratio;
			if(reportType == ReportType.LoanRate1
					|| reportType == ReportType.LoanRate2
					|| reportType == ReportType.LoanRate3
					|| reportType == ReportType.LoanRate4){
				strRatio = "������";
			}
			subMenu1.add(0, ITEM4, 0, strRatio);
		}
		SubMenu subMenu2 = subMenu1.addSubMenu(0,ITEM5,0,chartType);
		subMenu1.addSubMenu(0,ITEM9,0,R.string.back);
		subMenu2.add(0,ITEM6,0,getResources().getString(R.string.bar_chart));
		subMenu2.add(0,ITEM7,0,getResources().getString(R.string.pie_chart));
		subMenu2.add(0,ITEM8,0,getResources().getString(R.string.line_chart));
		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.ic_menu_view);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM1:
			content = count;
			showChart();
			break;
		case ITEM2:
			content = balance;
			showChart();
			break;
		case ITEM3:
			content = sum;
			showChart();
			break;
		case ITEM4:
			content = ratio;
			showChart();
			break;
		case ITEM6:
			typeOfChart = ChartType.BARCHART;
			showChart();
			break;
		case ITEM7:
			typeOfChart = ChartType.PIECHART;
			showChart();
			break;
		case ITEM8:
			typeOfChart = ChartType.LINECHART;
			showChart();
			break;
		case ITEM9:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ��ʾ����
	 * @param content ��������(��������ͬ�ȡ�����)
	 */
	private void showChart() {
		View chartView = null;
		try {
			if(typeOfChart==ChartType.BARCHART){
				//��״������
				BarChartFactory factory = new BarChartFactory(this);
				chartView = factory.getBarChartView(map, reportType, content);
			}else if(typeOfChart==ChartType.PIECHART){
				PieChartFactory factory = new PieChartFactory(this);
				chartView = factory.getPieChartView(map, reportType, content);
			}else if(typeOfChart==ChartType.LINECHART){
				LineChartFactory factory = new LineChartFactory(this);
				chartView = factory.getLineChartView(map, reportType, content);
			}
		} catch (Exception e) {
			e.printStackTrace();
			exit();
		}
		if(chartView!=null){
			//�����ͼ��������
			LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layout.removeAllViews();
			layout.addView(chartView, p);
		}
	}

	
	private void exit(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("�������ݴ��ڴ���");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
			}
		});
		builder.create().show();
	}
}
