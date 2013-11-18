package net.chinawuyue.mls.after_loan;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ��������ر������ݽṹ���״Σ�
 * @author Administrator
 *
 */
public class AfterLoanObject {
	
	//�����б�
	/**
	 * �ͻ����
	 */
	private String customerID;
	
	/**
	 * ��ݱ��
	 */
	private String serialNO;
	
	/**
	 * �ͻ�����
	 */
	private String customerName;
	
	/**
	 * ҵ��Ʒ��
	 */
	private String businessType;
	
	/**
	 * �ſ���
	 */
	private String businessSUM;
	
	/**
	 * �������
	 */
	private String balance;
	
	/**
	 * ��ʼ��
	 */
	private String putoutDate;
	
	/**
	 * ������
	 */
	private String maturity;
	
	/**
	 * �������ֹ����
	 */
	private String inspectDateName;
	
	/**
	 * �����
	 */
	private String inputUserName;
	
	/**
	 * ������
	 */
	private String inputOrgName;
	
	/**
	 * ��������ˮ��
	 */
	private String iiSerialNo;
	

	public AfterLoanObject() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * ������
	 * @param jsonData ����Ϊjson��ʽ�Ĵ�������
	 */
	public AfterLoanObject(String jsonData){
		//��������ص�json���ݣ���ʼ������������
		paserJsonData(jsonData);
	}
	
	//��������ص�json���ݣ���ʼ������������
	private void paserJsonData(String jsonData){
		try {
			JSONObject jsonObj = new JSONObject(jsonData);
			this.balance = jsonObj.optString("BALANCE", "");
			this.businessSUM = jsonObj.optString("BUSINESSSUM", "");
			this.businessType = jsonObj.optString("BUSINESSTYPE", "");
			this.customerID = jsonObj.optString("CUSTOMERID", "");
			this.customerName = jsonObj.optString("CUSTOMERNAME", "");
			this.inputOrgName = jsonObj.optString("INPUTORGNAME", "");
			this.inputUserName = jsonObj.optString("INPUTUSERNAME", "");
			this.inspectDateName = jsonObj.optString("INSPECTDATENAME", "");
			this.maturity = jsonObj.optString("MATURITY", "");
			this.putoutDate = jsonObj.optString("PUTOUTDATE", "");
			this.serialNO = jsonObj.optString("SERIALNO", "");
			this.iiSerialNo = jsonObj.optString("IISERIALNO", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getCustomerID() {
		return customerID;
	}


	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}


	public String getSerialNO() {
		return serialNO;
	}


	public void setSerialNO(String serialNO) {
		this.serialNO = serialNO;
	}


	public String getCustomerName() {
		return customerName;
	}


	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	public String getBusinessType() {
		return businessType;
	}


	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}


	public String getBusinessSUM() {
		return businessSUM;
	}


	public void setBusinessSUM(String businessSUM) {
		this.businessSUM = businessSUM;
	}


	public String getBalance() {
		return balance;
	}


	public void setBalance(String balance) {
		this.balance = balance;
	}


	public String getPutoutDate() {
		return putoutDate;
	}


	public void setPutoutDate(String putoutDate) {
		this.putoutDate = putoutDate;
	}


	public String getMaturity() {
		return maturity;
	}


	public void setMaturity(String maturity) {
		this.maturity = maturity;
	}


	public String getInspectDateName() {
		return inspectDateName;
	}


	public void setInspectDateName(String inspectDateName) {
		this.inspectDateName = inspectDateName;
	}


	public String getInputUserName() {
		return inputUserName;
	}


	public void setInputUserName(String inputUserName) {
		this.inputUserName = inputUserName;
	}


	public String getInputOrgName() {
		return inputOrgName;
	}


	public void setInputOrgName(String inputOrgName) {
		this.inputOrgName = inputOrgName;
	}

	public String getIiSerialNo() {
		return iiSerialNo;
	}

	public void setIiSerialNo(String iiSerialNo) {
		this.iiSerialNo = iiSerialNo;
	}
	
	
}

