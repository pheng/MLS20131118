
package net.chinawuyue.mls.before_loan;


import org.json.JSONObject;

/**
 * ��������ҵ���б����ݽṹ
 * @author Administrator
 *
 */
public class LoanDataObject {
	
	/**
	 * ������ˮ��
	 */
	private String serialNO;

	public String getManageUserName() {
		return manageUserName;
	}

	public void setManageUserName(String manageUserName) {
		this.manageUserName = manageUserName;
	}

	/**
	 * �ͻ����
	 */
	private String customerID;
	
	/**
	 * �ͻ�����
	 */
	private String customerName;
	
	/**
	 * ҵ��Ʒ��
	 */
	private String businessType;
	
	/**
	 * ������
	 */
	private String businessSUM;
	
	/**
	 * ��������
	 * not necessary
	 */
	private String flowNO;
	
	/**
	 * �׶α��
	 * not necessary
	 */
	private String phaseNO;
	
	/**
	 * ��������
	 */
	private String objectType;
	
	/**
	 * �ܻ���
	 */
	private String manageUserName;
	
	/**
	 * �ܻ�����
	 */
	private String manageOrgName;

	/**
	 * ��������
	 */
	private String inputDate;
	
	public LoanDataObject() {
		
	}
	
	public LoanDataObject(String jsonData) {
		parseJsonData(jsonData);
	}
	
	private void parseJsonData(String jsonData) {
		try {
			JSONObject jsonObj = new JSONObject(jsonData);
			this.businessSUM = jsonObj.optString("BUSINESSSUM", "");
			this.businessType = jsonObj.optString("BUSINESSTYPE", "");
			this.customerID = jsonObj.optString("CUSTOMERID", "");
			this.customerName = jsonObj.optString("CUSTOMERNAME", "");
			this.flowNO = jsonObj.optString("FLOWNO", "");
			this.inputDate = jsonObj.optString("INPUTDATE", "");
			this.manageOrgName = jsonObj.optString("MANAGEORGNAME", "");
			this.objectType = jsonObj.optString("OBJECTTYPE", "");
			this.phaseNO = jsonObj.optString("PHASENO", "");
			this.serialNO = jsonObj.optString("SERIALNO", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSerialNO() {
		return serialNO;
	}

	public void setSerialNO(String serialNO) {
		this.serialNO = serialNO;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
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

	public String getFlowNO() {
		return flowNO;
	}

	public void setFlowNO(String flowNO) {
		this.flowNO = flowNO;
	}

	public String getPhaseNO() {
		return phaseNO;
	}

	public void setPhaseNO(String phaseNO) {
		this.phaseNO = phaseNO;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getManageOrgName() {
		return manageOrgName;
	}

	public void setManageOrgName(String managerOrgName) {
		this.manageOrgName = managerOrgName;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}
}

