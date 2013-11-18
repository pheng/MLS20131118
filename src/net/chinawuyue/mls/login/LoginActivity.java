package net.chinawuyue.mls.login;

import java.io.File;
import java.io.Serializable;

import net.chinawuyue.mls.MainActivity;
import net.chinawuyue.mls.R;
import net.chinawuyue.mls.undotask.UndoTaskService;
import net.chinawuyue.mls.util.ActivityUtil;
import net.chinawuyue.mls.util.HttpUtil;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
/**
 * ��¼����
 * */
public class LoginActivity extends SherlockActivity {

	/** ��¼��Ϣ */
	private LoginInfo loginInfo = new LoginInfo();
	/** ������UI */
	private EditText view_userName;
	private EditText view_password;
	private CheckBox view_rememberMe;
	private Button view_loginSubmit;

	/** ��������SharePreferences�ı�ʶ */
	private final String SHARE_LOGIN_TAG = "SHARE_LOGIN_TAG";

	/** �����¼�ɹ���,���ڱ����û�����SharedPreferences,�Ա��´β������� */
	private String SHARE_LOGIN_USERNAME = "LOGIN_USERNAME";

	/** �����¼�ɹ���,���ڱ���PASSWORD��SharedPreferences,�Ա��´β������� */
	private String SHARE_LOGIN_PASSWORD = "LOGIN_PASSWORD";

	/** �����½ʧ��,������Ը��û�ȷ�е���Ϣ��ʾ,true����������ʧ��,false���û������������ */
	private boolean isNetError = false, isUpdate = false;

	/** ��¼loading��ʾ�� */
	private ProgressDialog proDialog;

	/** �Ƿ�ǰ��û���¼*/
	private boolean isNoBindingUserLogin;
	
	/** �Ƿ�ͬ�û���¼*/
	public static boolean isNewUserLogin;
	private String userName;
	/** �豸��*/
	private String deviceId;
	private HttpUtil httpUtil = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//����豸��
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
		deviceId = tm.getDeviceId();
		System.out.println(deviceId);
		
		view_userName = (EditText) findViewById(R.id.login_edit_account);
		view_password = (EditText) findViewById(R.id.login_edit_pwd);
		view_rememberMe = (CheckBox) findViewById(R.id.login_cb_savepwd);
		view_loginSubmit = (Button) findViewById(R.id.login_btn_login);

		initView(false);
		// ��ҪȥsubmitListener��������URL
		view_loginSubmit.setOnClickListener(submitListener);
		// ��ӱ����嵽�����У�����һ��ر�
		ActivityUtil.activityList.add(this);
		httpUtil = new HttpUtil();
	}

	/** ����ϴ����˱��棬���ȡ��ֵ */
	private void initView(boolean isRememberMe) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		userName = share.getString(SHARE_LOGIN_USERNAME, "");
		if (!"".equals(userName)) {
			view_userName.setText(userName);
			view_rememberMe.setChecked(true);
		}
		share = null;
	}

	/** ��¼Button Listener */
	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (checkInfo()) {
				proDialog = ProgressDialog.show(LoginActivity.this, "�û���¼",
						"�������ӷ����������Ժ�....", true, true);
				// ��һ���߳̽��е�¼��֤,��Ҫ������ʧ��,�ɹ�����ֱ��ͨ��startAcitivity(Intent)ת��
				Thread loginThread = new Thread(new DoLogin());
				loginThread.start();
				proDialog.setOnCancelListener(new OnCancelListener(){
					@Override
					public void onCancel(DialogInterface arg0) {
						httpUtil.abort();
					}
				});
			}
		}
	};

	/** �����˻������� */
	private void saveSharePreferences() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit()
				.putString(SHARE_LOGIN_USERNAME,
						view_userName.getText().toString()).commit();
		share = null;
	}

	/** ������� */
	private void clearSharePreference() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString(SHARE_LOGIN_USERNAME, "").commit();
		share = null;
	}

	private boolean checkInfo() {
		if (view_userName.getText().toString().length() == 0) {
			view_userName.setError("�˺Ų���Ϊ��");
			return false;
		}
		if (view_password.getText().toString().length() == 0) {
			view_password.setError("���벻��Ϊ��");
			return false;
		}
		return true;
	}


	
	private boolean validateLocalLogin(String usercode, String password) {
		// ���ڱ�ǵ�½״̬
		boolean loginState = false;	
		String isLocalUser = null;
		// ����webservice
//		JSONObject jsonRes = HttpUtil.doLoginWithDeviceId(usercode, password,deviceId);
		JSONObject jsonRes = httpUtil.doLogin(usercode, password);
		if (jsonRes != null) {
			try {
				if(jsonRes.has("logininfo")){
					loginInfo.userCode = jsonRes.getJSONObject("logininfo").optString("Usercode");
					loginInfo.lastLoginDate = jsonRes.getJSONObject("logininfo").optString("Lastlogindate");
					loginInfo.versionCode = jsonRes.getJSONObject("logininfo").optString("versioncode");
					loginInfo.role = jsonRes.getJSONObject("logininfo").optString("Role");
					isLocalUser = jsonRes.getJSONObject("logininfo").optString("isLocalUser");
				}
				if(jsonRes.has("userinfo")&&"1".equals(isLocalUser)){
					loginInfo.userName = jsonRes.getJSONObject("userinfo").optString("USERNAME");
					loginInfo.count1 = jsonRes.getJSONObject("userinfo").optString("COUNT1");
					loginInfo.count2 = jsonRes.getJSONObject("userinfo").optString("COUNT2");
					loginInfo.orgId = jsonRes.getJSONObject("userinfo").optString("ORGID");
					loginInfo.orgName = jsonRes.getJSONObject("userinfo").optString("ORGNAME");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (loginInfo.userCode!=null&&!"".equals(loginInfo.userCode) && !"-1".equals(loginInfo.userCode)) {
				loginState = true;
				// ���豸�󶨵��û� ���ܵ�¼
				if(!"1".equals(isLocalUser)){
					isNoBindingUserLogin = true;
					loginState = false;
				}
				if (loginInfo.versionCode!=null&&
						loginInfo.versionCode.length()>0 &&
						Integer.parseInt(loginInfo.versionCode) > this.getCurrentVersionCode())
					isUpdate = true;
			} else {
				loginState = false;
				isNetError = false;
				isUpdate = false;
			}
		} else {
			loginState = false;
			isNetError = true;
			isUpdate = false;
		}

		// ��½�ɹ�
		if (loginState) {
			if (view_rememberMe.isChecked()) {
				saveSharePreferences();
			} 
		} else {
			// ��������������
			if (!isNetError) {
				clearSharePreference();
			}
		}
		if (!view_rememberMe.isChecked()) {
			clearSharePreference();
		}
		return loginState;
	}

	/** ��¼��̨֪ͨ����UI�߳�,��Ҫ���ڵ�¼ʧ��,֪ͨUI�̸߳��½��� */
	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			isNetError = msg.getData().getBoolean("isNetError");
			isUpdate = msg.getData().getBoolean("isUpdate");
			isNoBindingUserLogin = msg.getData().getBoolean("isNewUserLogin");
			if (proDialog != null) {
				proDialog.dismiss();
			}
			if (isUpdate) {
				AlertDialog.Builder builder = new Builder(LoginActivity.this);
				builder.setMessage("�����°汾,��Ҫ����������");
				builder.setTitle("������ʾ");
				builder.setPositiveButton("��������",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								dialog.dismiss();								
								proDialog = ProgressDialog.show(
										LoginActivity.this, "Ӧ������",
										"���������°汾�����Ժ�....", true, true);
								Thread thread = new Thread(new Runnable(){
									@Override
									public void run() {
										httpUtil.downAPK(downHandler);
									}});
								thread.start();
							}
						});
				builder.setNegativeButton("�´���˵", new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								dialog.dismiss();
								
								//������̨�豸��������ѯ����
								Intent intentSer = new Intent();
								intentSer.setClass(LoginActivity.this, UndoTaskService.class);
								intentSer.putExtra("loginInfo", (Serializable)loginInfo);
								startService(intentSer);
								
								// ��Ҫ�������ݵ���½��Ľ���
								Intent intent = new Intent();
								intent.setClass(LoginActivity.this, MainActivity.class);
								intent.putExtra("loginInfo", (Serializable)loginInfo);
								// ת���½���ҳ��
								startActivity(intent);
								if(proDialog!=null)
									proDialog.dismiss();
							}
						});
				builder.create().show();
			} else {
				if (isNoBindingUserLogin){
					Toast.makeText(LoginActivity.this,"�Ǳ��豸���û����ܵ�¼",
							Toast.LENGTH_SHORT).show();
					return;
				} 
				if (isNetError) {
					Toast.makeText(LoginActivity.this,
							"��¼ʧ��,��ȷ���п��õ���������",
							Toast.LENGTH_SHORT).show();
				}
				// �û������������
				else {
					Toast.makeText(LoginActivity.this, "��¼ʧ��,��������ȷ���û���������.",
							Toast.LENGTH_SHORT).show();
					// �����ǰ��SharePreferences����
					clearSharePreference();
				}
			}
		}
	};

	class DoLogin implements Runnable {
		@Override
		public void run() {
			boolean loginState = false;
			String userName1 = view_userName.getText().toString();
			String password = view_password.getText().toString();
			loginState = validateLocalLogin(userName1, password);
			if (loginState && !isUpdate) {
				//�Ƿ�ͬ�û���¼
				isNewUserLogin = !userName.equals(userName1);
				
				//������̨�豸��������ѯ����
				Intent intentSer = new Intent();
				intentSer.setClass(LoginActivity.this, UndoTaskService.class);
				intentSer.putExtra("loginInfo", (Serializable)loginInfo);
				startService(intentSer);
				
				// ��Ҫ�������ݵ���½��Ľ���
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				intent.putExtra("loginInfo", (Serializable)loginInfo);
				// ת���½���ҳ��
				startActivity(intent);
				
				if(proDialog!=null){
					proDialog.dismiss();
				}
				
				LoginActivity.this.finish();
			} else{
				// ͨ������handler��֪ͨUI���̸߳���UI
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putBoolean("isNetError", isNetError);
				bundle.putBoolean("isUpdate", isUpdate);
				bundle.putBoolean("isNewUserLogin", isNoBindingUserLogin);
				message.setData(bundle);
				loginHandler.sendMessage(message);
			}
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			ActivityUtil.showExitDialog(LoginActivity.this);
			return false;
		}
		return false;
	};

	private int getCurrentVersionCode() {
		PackageManager manager = getPackageManager();
		String packageName = getPackageName();
		try {
			PackageInfo info = manager.getPackageInfo(packageName, 0);
			return info.versionCode;
		} catch (Exception e) {
			return 1;
		}
	};

	Handler downHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (proDialog != null) {
				proDialog.dismiss();
			}
			int isDown = msg.arg1;
			switch (isDown) {
			case 1:
				downHandler.post(new Runnable() {
					public void run() {
						installAPK();
					}
				});
				// Toast.makeText(LoginActivity.this,
				// "�°汾�������,���˳���װ.",Toast.LENGTH_LONG).show();
				break;
			case 0:
				Toast.makeText(LoginActivity.this, "�°汾����ʧ��,����ʱ����.",
						Toast.LENGTH_LONG).show();
				break;
			case 2:
				Toast.makeText(LoginActivity.this, "û�м�⵽SD��,��忨����.",
						Toast.LENGTH_LONG).show();
			}
		}
	};

	private void installAPK() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "MLS.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

}
