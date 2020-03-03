package webFrontCommonUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ACTLine {

	@SerializedName("code")
	@Expose
	private String code;
	@SerializedName("salesAmount")
	@Expose
	private String salesAmount;
	@SerializedName("summationOfSeveralTimeBlocks")
	@Expose
	private String summationOfSeveralTimeBlocks;
	@SerializedName("startingTime")
	@Expose
	private String startingTime;
	@SerializedName("ItemCounter")
	@Expose
	private String itemCounter;
	@SerializedName("transactionCustomerCounter")
	@Expose
	private String transactionCustomerCounter;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(String salesAmount) {
		this.salesAmount = salesAmount;
	}

	public String getSummationOfSeveralTimeBlocks() {
		return summationOfSeveralTimeBlocks;
	}

	public void setSummationOfSeveralTimeBlocks(String summationOfSeveralTimeBlocks) {
		this.summationOfSeveralTimeBlocks = summationOfSeveralTimeBlocks;
	}

	public String getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(String startingTime) {
		this.startingTime = startingTime;
	}

	public String getItemCounter() {
		return itemCounter;
	}

	public void setItemCounter(String itemCounter) {
		this.itemCounter = itemCounter;
	}

	public String getTransactionCustomerCounter() {
		return transactionCustomerCounter;
	}

	public void setTransactionCustomerCounter(String transactionCustomerCounter) {
		this.transactionCustomerCounter = transactionCustomerCounter;
	}

}
