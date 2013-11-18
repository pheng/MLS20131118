package net.chinawuyue.mls.board;

import java.util.Map;

import net.chinawuyue.mls.R;
import net.chinawuyue.mls.reports.BaseReport;
import net.chinawuyue.mls.reports.BaseReport.ReportType;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
/**
 * ����֪ͨ����
 */
public class BoardActivity extends SherlockActivity {

	private static final int ITEM1 = 1;
	private TextView tvTitle = null;
	private TextView tvTime = null;
	private TextView tvDesc = null;
	private Map<String, Object> map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board_detail);
		//��ñ�����洫�������ݺͱ�������
		map = (Map<String, Object>) getIntent().getExtras().get("map");
		if(map!=null)
			initViews();
	}

	private void initViews(){
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		tvTime = (TextView)findViewById(R.id.tvTime);
		tvDesc = (TextView)findViewById(R.id.tvDesc);
		tvTitle.setText("���⣺"+map.get(BaseReport.BOARDTITLE).toString());
		tvTime.setText("����ʱ�䣺"+map.get(BaseReport.INPUTDATE).toString());
		tvDesc.setText(map.get(BaseReport.BOARDDESC).toString());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu subMenu1 = menu.addSubMenu("");
		//���ݱ���ͬ��Ӳ�ͬ�Ĳ˵�
		subMenu1.add(0, ITEM1, 0, R.string.back);
		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.ic_menu_view);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case ITEM1:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
