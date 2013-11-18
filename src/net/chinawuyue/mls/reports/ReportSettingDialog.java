package net.chinawuyue.mls.reports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.chinawuyue.mls.R;
import net.chinawuyue.mls.login.LoginActivity;
import net.chinawuyue.mls.login.LoginInfo;
import net.chinawuyue.mls.reports.BaseReport.ReportType;
import net.chinawuyue.mls.reports.BusinessSurveyReport.MyAdapter;
import net.chinawuyue.mls.util.DoFetchThread;
import net.simonvt.menudrawer.MenuDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �������öԻ���
 * 
 */
public class ReportSettingDialog {
	public static final String SHARE_TAG = "SHARE_REPORT_SETTING";
	public static final String SHARE_DEFAUT_ORGNAME = "SHARE_DEFAUT_ORGNAME";
	public static final String SHARE_DEFAUT_ORGID = "SHARE_DEFAUT_ORGID";
	public static final String SHARE_DEFAUT_MONTH = "SHARE_DEFAUT_MONTH";
	public static final String SHARE_DEFAUT_UNITNAME = "SHARE_DEFAUT_UNITNAME";
	public static final String SHARE_DEFAUT_YEAR = "SHARE_DEFAUT_YEAR";
	private Context context; // ������
	private View view; // �Ի�����ͼ
	private Spinner spOrgName; // �����˵� ����
	private Spinner spMonth; // �����˵� �·�
	private Spinner spYear; // �����˵� �·�
	private Spinner spUnitName; // �����˵� ��λ
	private CheckBox cbIsDefault; // ��ѡ�� �Ƿ񱣴�Ĭ��ֵ
	private BaseReport report; // �������
	private MenuDrawer menuDrawer; // ������˵�
	private List<OrgItem> orgItems = null; // �����б�
	private String queryOrgId;
	private String queryOrgName;
	private String month;
	private String year;
	private String unitName;
	private ProgressDialog progressDialog;
	private boolean isNetError = false;
	private LoginInfo loginInfo;

	public ReportSettingDialog(Context context, MenuDrawer menuDrawer,
			LoginInfo loginInfo) {
		this.context = context;
		this.menuDrawer = menuDrawer;
		this.loginInfo = loginInfo;
		if(LoginActivity.isNewUserLogin)
			clearDefault();
		readDefault();
	}

	/**
	 * ���ñ���
	 * 
	 * @param reportType
	 *            ��������
	 */
	public void setReport(ReportType reportType) {
		// ͨ���������ʹ�������������ͼ
		if (reportType == ReportType.LoanBalance) {
			report = new LoanBalanceReport(context);
			report.setReportView(R.layout.activity_report_loan_balance);
		} else if (reportType == ReportType.BusinessSurvey) {
			report = new BusinessSurveyReport(context);
			report.setReportView(R.layout.activity_report_business_survey);
		} else if (reportType == ReportType.SubjectBalance) {
			report = new SubjectBalanceReport(context);
			report.setReportView(R.layout.activity_report_subject_balance);
		} else if (reportType == ReportType.LoanAnalysis1) {
			report = new LoanAnalysis1Report(context);
			report.setReportView(R.layout.activity_report_loan_analysis1);
		} else if (reportType == ReportType.LoanAnalysis2) {
			report = new LoanAnalysis2Report(context);
			report.setReportView(R.layout.activity_report_loan_analysis2);
		} else if (reportType == ReportType.LoanAnalysis3) {
			report = new LoanAnalysis3Report(context);
			report.setReportView(R.layout.activity_report_loan_analysis3);
		} else if (reportType == ReportType.LoanRate1) {
			report = new LoanRate1Report(context);
			report.setReportView(R.layout.activity_report_loan_rate1);
		} else if (reportType == ReportType.LoanRate2) {
			report = new LoanRate2Report(context);
			report.setReportView(R.layout.activity_report_loan_rate2);
		} else if (reportType == ReportType.LoanRate3) {
			report = new LoanRate3Report(context);
			report.setReportView(R.layout.activity_report_loan_rate3);
		} else if (reportType == ReportType.LoanRate4) {
			report = new LoanRate4Report(context);
			report.setReportView(R.layout.activity_report_loan_rate4);
		} 
	}

	/**
	 * ���ر���
	 * 
	 * @return
	 */
	public BaseReport getReport() {
		return this.report;
	}

	/**
	 * ��ʾ�Ի���
	 */
	public void showDialog() {
		// ������ͼ
		view = LayoutInflater.from(context).inflate(
				R.layout.dialog_choose_superorg, null);
		spOrgName = (Spinner) view.findViewById(R.id.spOrgName);
		spMonth = (Spinner) view.findViewById(R.id.spMonth);
		spYear = (Spinner) view.findViewById(R.id.spYear);
		spUnitName = (Spinner) view.findViewById(R.id.spUnitName);
		cbIsDefault = (CheckBox) view.findViewById(R.id.cbIsDefault);
		// �����Ի���
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder	.setView(view)
				.setCancelable(false)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						if (isNetError) {
							Toast.makeText(context, "���ݶ�ȡʧ��,������������", 1).show();
							return;
						}
						// ����·ݺͻ������
						if(spMonth.getAdapter().getCount()>0)
							month = spMonth.getSelectedItem().toString();
						if(spYear.getAdapter().getCount()>0)
							year = spYear.getSelectedItem().toString();
						if(spUnitName.getAdapter().getCount()>0)
							unitName = spUnitName.getSelectedItem().toString();
						Object obj = null;
						if(spMonth.getAdapter().getCount()>0)	
							obj = spOrgName.getSelectedItem();
						if (obj == null)
							return;
						queryOrgId = obj.toString();
						// �Ƿ񱣴�Ĭ������
						if (cbIsDefault.isChecked()) {
							saveDefault();
						} 
						report.isNeedUpdate = true;
						showReport();
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		builder.create().show();
		// ���������·�
		fetchData();
	}

	/** ��ʾ���� */
	public void showReport() {
		ReportRequest request = new ReportRequest();
		// ����������
		request.setDATE(year + "/" + (month.length() < 2 ? "0" + month : month));
		request.setORGID(loginInfo.orgId);
		request.setQUERYORGID(queryOrgId);
		request.setUNITNAME(unitName);
		request.setUSERID(loginInfo.userCode);
		ChartActivity.unitName = unitName;
		// ���ͱ������ɱ���
		report.setReportRequest(request);
		View viewReport = report.getReportView();
		menuDrawer.removeView(viewReport);
		menuDrawer.setContentView(viewReport);
	}

	/** ��ȡ�����������˵� */
	public void fetchData() {
		progressDialog = ProgressDialog.show(context, "", context
				.getResources().getString(R.string.wait),true,true);
		handler.postDelayed(new Runnable() {
			public void run() {
				if (orgItems == null || orgItems.size() == 0) {
					JSONObject request = new JSONObject();
					try {
						request.put("userId", loginInfo.userCode);
						request.put("orgId", loginInfo.orgId);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					final DoFetchThread doFetch = new DoFetchThread("RP0001", handler,request);
					Thread thread = new Thread(doFetch);
					thread.start();
					progressDialog.setOnCancelListener(new OnCancelListener(){
						@Override
						public void onCancel(DialogInterface dialog) {
							doFetch.stop();
						}
					});
				} else {
					if (progressDialog != null) {
						progressDialog.dismiss();
					}
					setAdapter(new OrgAdapter(orgItems));
				}
			}
		}, BaseReport.DELAY);
	}

	/** ������ */
	class OrgItem {
		public String orgName; // ��������
		public String orgId; // �������
	}

	/** ���������� */
	class OrgAdapter extends BaseAdapter {

		private List<OrgItem> orgInfo;

		public OrgAdapter(List<OrgItem> items) {
			this.orgInfo = items;
		}

		@Override
		public int getCount() {
			return orgInfo.size();
		}

		@Override
		public Object getItem(int pos) {
			return orgInfo.get(pos).orgId;
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int pos, View contentView, ViewGroup arg2) {
			TextView v = null;
			if (contentView == null) {
				v = (TextView) LayoutInflater.from(context).inflate(
						R.layout.spinner_org, null);
			} else {
				v = (TextView) contentView;
			}
			v.setText(orgInfo.get(pos).orgName);
			v.setTag(orgInfo.get(pos).orgId);
			return v;
		}
	}

	/**
	 * ����������
	 * 
	 * @param adapter
	 */
	private void setAdapter(OrgAdapter adapter) {
		// ������������
		Integer[] months = new Integer[12];
		for (int i = 0; i < months.length; i++) {
			months[i] = i + 1;
		}
		ArrayAdapter<Integer> aaMonths = new ArrayAdapter<Integer>(context,
				R.layout.spinner_org, months);
		spMonth.setAdapter(aaMonths);
		// ������������
		Integer[] years = new Integer[10];
		int y = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = 0; i < 10; i++) {
			years[i] = y - i;
		}
		ArrayAdapter<Integer> aaYear = new ArrayAdapter<Integer>(context,
				R.layout.spinner_org, years);
		spYear.setAdapter(aaYear);
		// ���õ�λ������
		String[] unitNames = { "��Ԫ", "����Ԫ", "��Ԫ", "ǧԪ", "Ԫ" };
		ArrayAdapter<String> aaUnitnames = new ArrayAdapter<String>(context,
				R.layout.spinner_org, unitNames);
		spUnitName.setAdapter(aaUnitnames);
		// ���û���������
		spOrgName.setAdapter(adapter);
		// ��ʾĬ��ֵ
		setSpinnerSelection(spOrgName, queryOrgId);
		setSpinnerSelection(spYear, year);
		setSpinnerSelection(spMonth, month);
		setSpinnerSelection(spUnitName, unitName);
	}

	/**
	 * ����Spinnerѡ����
	 * @param sp
	 * @param item
	 */
	private void setSpinnerSelection(Spinner sp, String item) {
		if(item==null)
			return;
		int position = 0;
		for (int i = 0; i < sp.getCount(); i++) {
			String s = sp.getItemAtPosition(i).toString();
			if (item.equals(s)) {
				position = i;
				break;
			}
		}
		sp.setSelection(position);
	}

	/**
	 * �̴߳���
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			if (msg.what == -1) {
				Toast.makeText(context, R.string.error, 1).show();
				isNetError = true;
				return;
			}
			isNetError = false;
			String json = msg.obj.toString();
			try {
				orgItems = parseJSON(json);
				setAdapter(new OrgAdapter(orgItems));
				if(orgItems.size()==0){
					Toast.makeText(context, R.string.empty_org, 1).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	/**
	 * ����JSON����
	 * 
	 * @param json
	 * @return
	 */
	private List<OrgItem> parseJSON(String json) {
		List<OrgItem> items = new ArrayList<OrgItem>();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray array = obj.optJSONArray("ARRAY1");
			if(array!=null)
			for (int i = 0; i < array.length(); i++) {
				JSONObject data = array.getJSONObject(i);
				OrgItem item = new OrgItem();
				item.orgId = data.optString(BaseReport.QUERYORGID);
				item.orgName = data.optString(BaseReport.QUERYORGNAME);
				items.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * ����Ĭ������
	 */
	public void saveDefault() {
		Editor editor = context.getSharedPreferences(SHARE_TAG, 0).edit();
		editor.putString(SHARE_DEFAUT_ORGNAME, queryOrgName);
		editor.putString(SHARE_DEFAUT_ORGID, queryOrgId);
		editor.putString(SHARE_DEFAUT_MONTH, month);
		editor.putString(SHARE_DEFAUT_UNITNAME, unitName);
		editor.putString(SHARE_DEFAUT_YEAR, year);
		editor.commit();
		editor = null;
	}

	/**
	 * ��ȡĬ��ֵ
	 */
	public void readDefault() {
		SharedPreferences sf = context.getSharedPreferences(SHARE_TAG, 0);
		this.queryOrgId = sf.getString(SHARE_DEFAUT_ORGID, "");
		this.queryOrgName = sf.getString(SHARE_DEFAUT_ORGNAME, "");
		this.month = sf.getString(SHARE_DEFAUT_MONTH, "");
		this.unitName = sf.getString(SHARE_DEFAUT_UNITNAME, "");
		this.year = sf.getString(SHARE_DEFAUT_YEAR, "");
	}

	/**
	 * ���Ĭ��ֵ
	 */
	public void clearDefault() {
		Editor editor = context.getSharedPreferences(SHARE_TAG, 0).edit();
		editor.putString(SHARE_DEFAUT_ORGNAME, "");
		editor.putString(SHARE_DEFAUT_ORGID, "");
		editor.putString(SHARE_DEFAUT_MONTH, "");
		editor.putString(SHARE_DEFAUT_UNITNAME, "");
		editor.putString(SHARE_DEFAUT_YEAR, "");
		editor.commit();
		editor = null;
	}

	public String getOrgId() {
		return queryOrgId;
	}
	
}
