package webFrontCommonUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class REGLine {

	@SerializedName("info1-info2")
	@Expose
	private String info1Info2;
	@SerializedName("amount-sales")
	@Expose
	private String amountSales;
	@SerializedName("percent")
	@Expose
	private String percent;
	@SerializedName("transactionCounter")
	@Expose
	private String transactionCounter;
	@SerializedName("itemCounter")
	@Expose
	private String itemCounter;
	@SerializedName("itemCode-subcode")
	@Expose
	private String itemCodeSubcode;
	@SerializedName("info3")
	@Expose
	private String info3;
	@SerializedName("textField")
	@Expose
	private String textField;

	public String getInfo1Info2() {
		return info1Info2;
	}

	public void setInfo1Info2(String info1Info2) {
		this.info1Info2 = info1Info2;
	}

	public String getAmountSales() {
		return amountSales;
	}

	public void setAmountSales(String amountSales) {
		this.amountSales = amountSales;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getTransactionCounter() {
		return transactionCounter;
	}

	public void setTransactionCounter(String transactionCounter) {
		this.transactionCounter = transactionCounter;
	}

	public String getItemCounter() {
		return itemCounter;
	}

	public void setItemCounter(String itemCounter) {
		this.itemCounter = itemCounter;
	}

	public String getItemCodeSubcode() {
		return itemCodeSubcode;
	}

	public void setItemCodeSubcode(String itemCodeSubcode) {
		this.itemCodeSubcode = itemCodeSubcode;
	}

	public String getInfo3() {
		return info3;
	}

	public void setInfo3(String info3) {
		this.info3 = info3;
	}

	public String getTextField() {
		return textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

}