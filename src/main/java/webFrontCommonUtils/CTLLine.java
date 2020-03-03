package webFrontCommonUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CTLLine {

	@SerializedName("flag")
	@Expose
	private String flag = "00";
	@SerializedName("operatorNo")
	@Expose
	private String operatorNo = "0000";
	@SerializedName("secretNo11")
	@Expose
	private String secretNo11 = "000000000000000000000000";
	@SerializedName("secretNo10")
	@Expose
	private String secretNo10 = "000000000000000000000000";
	@SerializedName("secretNo12")
	@Expose
	private String secretNo12 = "000000000000000000000000";
	@SerializedName("authorization")
	@Expose
	private String authorization = "000";
	@SerializedName("time1")
	@Expose
	private String time1 = "0000";
	@SerializedName("time2")
	@Expose
	private String time2 = "0000";
	@SerializedName("status_ActionCode")
	@Expose
	private String statusActionCode = "00";
	@SerializedName("time3")
	@Expose
	private String time3 = "0000";
	@SerializedName("password")
	@Expose
	private String password = "000000000000000000000000";
	@SerializedName("time4")
	@Expose
	private String time4 = "0000";
	@SerializedName("lockIndicator")
	@Expose
	private String lockIndicator = "0";
	@SerializedName("secretNumber")
	@Expose
	private String secretNumber = "0000";
	@SerializedName("signInPermittedTerminal")
	@Expose
	private String signInPermittedTerminal = "000";
	@SerializedName("cashDrawerNo")
	@Expose
	private String cashDrawerNo = "000";
	@SerializedName("POSTerminalNumber")
	@Expose
	private String pOSTerminalNumber = "000";
	@SerializedName("secretNo9")
	@Expose
	private String secretNo9 = "000000000000000000000000";
	@SerializedName("profile")
	@Expose
	private String profile = "000";
	@SerializedName("secretNo4")
	@Expose
	private String secretNo4 = "000000000000000000000000";
	@SerializedName("date4")
	@Expose
	private String date4 = "000000";
	@SerializedName("secretNo3")
	@Expose
	private String secretNo3 = "000000000000000000000000";
	@SerializedName("dateOfBirth")
	@Expose
	private String dateOfBirth = "000000";
	@SerializedName("date3")
	@Expose
	private String date3 = "000000";
	@SerializedName("secretNo2")
	@Expose
	private String secretNo2 = "000000000000000000000000";
	@SerializedName("date2")
	@Expose
	private String date2 = "000000";
	@SerializedName("secretNo1")
	@Expose
	private String secretNo1 = "000000000000000000000000";
	@SerializedName("date1")
	@Expose
	private String date1 = "000000";
	@SerializedName("secretNo8")
	@Expose
	private String secretNo8 = "000000000000000000000000";
	@SerializedName("secretNo7")
	@Expose
	private String secretNo7 = "000000000000000000000000";
	@SerializedName("secretNo6")
	@Expose
	private String secretNo6 = "000000000000000000000000";
	@SerializedName("personnelNo")
	@Expose
	private String personnelNo = "00000000";
	@SerializedName("secretNo5")
	@Expose
	private String secretNo5 = "000000000000000000000000";
	@SerializedName("name")
	@Expose
	private String name = "                    xxxxxxxxxx";
	@SerializedName("wrongEntries")
	@Expose
	private String wrongEntries = "00";

	public CTLLine () {
	}
		
	public CTLLine (String operatorNo) {
		this.operatorNo = operatorNo;		
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorNo(String operatorNo) {
		this.operatorNo = operatorNo;
	}

	public String getSecretNo11() {
		return secretNo11;
	}

	public void setSecretNo11(String secretNo11) {
		this.secretNo11 = secretNo11;
	}

	public String getSecretNo10() {
		return secretNo10;
	}

	public void setSecretNo10(String secretNo10) {
		this.secretNo10 = secretNo10;
	}

	public String getSecretNo12() {
		return secretNo12;
	}

	public void setSecretNo12(String secretNo12) {
		this.secretNo12 = secretNo12;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public String getTime1() {
		return time1;
	}

	public void setTime1(String time1) {
		this.time1 = time1;
	}

	public String getTime2() {
		return time2;
	}

	public void setTime2(String time2) {
		this.time2 = time2;
	}

	public String getStatusActionCode() {
		return statusActionCode;
	}

	public void setStatusActionCode(String statusActionCode) {
		this.statusActionCode = statusActionCode;
	}

	public String getTime3() {
		return time3;
	}

	public void setTime3(String time3) {
		this.time3 = time3;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTime4() {
		return time4;
	}

	public void setTime4(String time4) {
		this.time4 = time4;
	}

	public String getLockIndicator() {
		return lockIndicator;
	}

	public void setLockIndicator(String lockIndicator) {
		this.lockIndicator = lockIndicator;
	}

	public String getSecretNumber() {
		return secretNumber;
	}

	public void setSecretNumber(String secretNumber) {
		this.secretNumber = secretNumber;
	}

	public String getSignInPermittedTerminal() {
		return signInPermittedTerminal;
	}

	public void setSignInPermittedTerminal(String signInPermittedTerminal) {
		this.signInPermittedTerminal = signInPermittedTerminal;
	}

	public String getCashDrawerNo() {
		return cashDrawerNo;
	}

	public void setCashDrawerNo(String cashDrawerNo) {
		this.cashDrawerNo = cashDrawerNo;
	}

	public String getPOSTerminalNumber() {
		return pOSTerminalNumber;
	}

	public void setPOSTerminalNumber(String pOSTerminalNumber) {
		this.pOSTerminalNumber = pOSTerminalNumber;
	}

	public String getSecretNo9() {
		return secretNo9;
	}

	public void setSecretNo9(String secretNo9) {
		this.secretNo9 = secretNo9;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getSecretNo4() {
		return secretNo4;
	}

	public void setSecretNo4(String secretNo4) {
		this.secretNo4 = secretNo4;
	}

	public String getDate4() {
		return date4;
	}

	public void setDate4(String date4) {
		this.date4 = date4;
	}

	public String getSecretNo3() {
		return secretNo3;
	}

	public void setSecretNo3(String secretNo3) {
		this.secretNo3 = secretNo3;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDate3() {
		return date3;
	}

	public void setDate3(String date3) {
		this.date3 = date3;
	}

	public String getSecretNo2() {
		return secretNo2;
	}

	public void setSecretNo2(String secretNo2) {
		this.secretNo2 = secretNo2;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public String getSecretNo1() {
		return secretNo1;
	}

	public void setSecretNo1(String secretNo1) {
		this.secretNo1 = secretNo1;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getSecretNo8() {
		return secretNo8;
	}

	public void setSecretNo8(String secretNo8) {
		this.secretNo8 = secretNo8;
	}

	public String getSecretNo7() {
		return secretNo7;
	}

	public void setSecretNo7(String secretNo7) {
		this.secretNo7 = secretNo7;
	}

	public String getSecretNo6() {
		return secretNo6;
	}

	public void setSecretNo6(String secretNo6) {
		this.secretNo6 = secretNo6;
	}

	public String getPersonnelNo() {
		return personnelNo;
	}

	public void setPersonnelNo(String personnelNo) {
		this.personnelNo = personnelNo;
	}

	public String getSecretNo5() {
		return secretNo5;
	}

	public void setSecretNo5(String secretNo5) {
		this.secretNo5 = secretNo5;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWrongEntries() {
		return wrongEntries;
	}

	public void setWrongEntries(String wrongEntries) {
		this.wrongEntries = wrongEntries;
	}

}
