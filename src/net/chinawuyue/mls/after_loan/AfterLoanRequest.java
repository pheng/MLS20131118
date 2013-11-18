package net.chinawuyue.mls.after_loan;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ����������������ݽṹ
 * @author Administrator
 *
 */
public class AfterLoanRequest {
	
	public AfterLoanRequest() {
	}

	class ListRequest{
		/**
		 * ������
		 */
		private String CODENO = null;
		
		/**
		 * �û����
		 */
		private String USERID = null;
		
		/**
		 * ҵ������
		 * 010 �����
		 * 020 �����
		 * 030 ����δ���
		 */
		private String INSPECTTYPE = null;

		public ListRequest() {
		}
		
		public ListRequest(String CODENO, String USERID, String INSPECTTYPE){
			this.CODENO = CODENO;
			this.USERID = USERID;
			this.INSPECTTYPE = INSPECTTYPE;
		}
		
		public String getCODENO() {
			return CODENO;
		}

		public void setCODENO(String cODENO) {
			CODENO = cODENO;
		}

		public String getUSERID() {
			return USERID;
		}

		public void setUSERID(String uSERID) {
			USERID = uSERID;
		}

		public String getINSPECTTYPE() {
			return INSPECTTYPE;
		}

		public void setINSPECTTYPE(String iNSPECTTYPE) {
			INSPECTTYPE = iNSPECTTYPE;
		}
		
		public JSONObject jsonRequest(){
			JSONObject obj = new JSONObject();
			try {
				obj.put("CODENO", CODENO);
				obj.put("userId", USERID);
				obj.put("inspectType", INSPECTTYPE);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return obj;
		}
	} 
	
	class ReportRequest{
		private String CODENO= "AL0003";
		private String OBJECTNO = null;
		private String OBJECTTYPE = null;
		
		public ReportRequest() {
		}
		
		public ReportRequest(String OBJECTNO, String OBJECTTYPE) {
			this.OBJECTNO = OBJECTNO;
			this.OBJECTTYPE = OBJECTTYPE;
		}
		
		/**
		 * ��鱨��������
		 * @return JSON��ʽ�ļ�鱨�������ĵ��ַ���
		 */
		public JSONObject jsonRequest(){
			JSONObject obj = new JSONObject();
			try {
				obj.put("CODENO", CODENO);
				obj.put("objectNo", OBJECTNO);
				obj.put("objectType", OBJECTTYPE);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return obj;
		}
		
		public String getCODENO() {
			return CODENO;
		}
		public void setCODENO(String cODENO) {
			CODENO = cODENO;
		}
		public String getOBJECTNO() {
			return OBJECTNO;
		}
		public void setOBJECTNO(String oBJECTNO) {
			OBJECTNO = oBJECTNO;
		}
		public String getOBJECTTYPE() {
			return OBJECTTYPE;
		}
		public void setOBJECTTYPE(String oBJECTTYPE) {
			OBJECTTYPE = oBJECTTYPE;
		}
	}
}
