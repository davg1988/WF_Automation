package webFrontCommonUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DPTLine {

	@SerializedName("discountAllowed_VAT_Tax_code_category")
	@Expose
	private String discountAllowedVATTaxCodeCategory;
	@SerializedName("code3_code4_ageRestriction_type")
	@Expose
	private String code3Code4AgeRestrictionType;
	@SerializedName("itemCounterDiscount")
	@Expose
	private String itemCounterDiscount;
	@SerializedName("halo_lalo")
	@Expose
	private String haloLalo;
	@SerializedName("text_filler")
	@Expose
	private String textFiller;
	@SerializedName("itemCounter")
	@Expose
	private String itemCounter;
	@SerializedName("transactionCounter_employeeCustomerDiscount")
	@Expose
	private String transactionCounterEmployeeCustomerDiscount;
	@SerializedName("transactionCounter_autoDiscount_temDisc_priceOverride_mixMatch")
	@Expose
	private String transactionCounterAutoDiscountTemDiscPriceOverrideMixMatch;
	@SerializedName("employee_customerDiscount")
	@Expose
	private String employeeCustomerDiscount;
	@SerializedName("transactionCounter_CustomerCounter")
	@Expose
	private String transactionCounterCustomerCounter;
	@SerializedName("departmentNo")
	@Expose
	private String departmentNo;
	@SerializedName("grossSales")
	@Expose
	private String grossSales;
	@SerializedName("itemCounter_employeeCustomerDiscount")
	@Expose
	private String itemCounterEmployeeCustomerDiscount;
	@SerializedName("code1_code2")
	@Expose
	private String code1Code2;
	@SerializedName("nextHigherAccumulationLevel")
	@Expose
	private String nextHigherAccumulationLevel;
	@SerializedName("autoDiscount_itemDisc_priceOverride_mixMatch")
	@Expose
	private String autoDiscountItemDiscPriceOverrideMixMatch;

	public String getDiscountAllowedVATTaxCodeCategory() {
		return discountAllowedVATTaxCodeCategory;
	}

	public void setDiscountAllowedVATTaxCodeCategory(String discountAllowedVATTaxCodeCategory) {
		this.discountAllowedVATTaxCodeCategory = discountAllowedVATTaxCodeCategory;
	}

	public String getCode3Code4AgeRestrictionType() {
		return code3Code4AgeRestrictionType;
	}

	public void setCode3Code4AgeRestrictionType(String code3Code4AgeRestrictionType) {
		this.code3Code4AgeRestrictionType = code3Code4AgeRestrictionType;
	}

	public String getHaloLalo() {
		return haloLalo;
	}

	public void setHaloLalo(String haloLalo) {
		this.haloLalo = haloLalo;
	}

	public String getTextFiller() {
		return textFiller;
	}

	public void setTextFiller(String textFiller) {
		this.textFiller = textFiller;
	}

	public String getItemCounter() {
		return itemCounter;
	}

	public void setItemCounter(String itemCounter) {
		this.itemCounter = itemCounter;
	}

	public String getTransactionCounterEmployeeCustomerDiscount() {
		return transactionCounterEmployeeCustomerDiscount;
	}

	public void setTransactionCounterEmployeeCustomerDiscount(String transactionCounterEmployeeCustomerDiscount) {
		this.transactionCounterEmployeeCustomerDiscount = transactionCounterEmployeeCustomerDiscount;
	}

	public String getTransactionCounterAutoDiscountTemDiscPriceOverrideMixMatch() {
		return transactionCounterAutoDiscountTemDiscPriceOverrideMixMatch;
	}

	public void setTransactionCounterAutoDiscountTemDiscPriceOverrideMixMatch(String transactionCounterAutoDiscountTemDiscPriceOverrideMixMatch) {
		this.transactionCounterAutoDiscountTemDiscPriceOverrideMixMatch = transactionCounterAutoDiscountTemDiscPriceOverrideMixMatch;
	}

	public String getEmployeeCustomerDiscount() {
		return employeeCustomerDiscount;
	}

	public void setEmployeeCustomerDiscount(String employeeCustomerDiscount) {
		this.employeeCustomerDiscount = employeeCustomerDiscount;
	}

	public String getTransactionCounterCustomerCounter() {
		return transactionCounterCustomerCounter;
	}

	public void setTransactionCounterCustomerCounter(String transactionCounterCustomerCounter) {
		this.transactionCounterCustomerCounter = transactionCounterCustomerCounter;
	}

	public String getDepartmentNo() {
		return departmentNo;
	}

	public void setDepartmentNo(String departmentNo) {
		this.departmentNo = departmentNo;
	}

	public String getGrossSales() {
		return grossSales;
	}

	public void setGrossSales(String grossSales) {
		this.grossSales = grossSales;
	}

	public String getItemCounterEmployeeCustomerDiscount() {
		return itemCounterEmployeeCustomerDiscount;
	}

	public void setItemCounterEmployeeCustomerDiscount(String itemCounterEmployeeCustomerDiscount) {
		this.itemCounterEmployeeCustomerDiscount = itemCounterEmployeeCustomerDiscount;
	}

	public String getCode1Code2() {
		return code1Code2;
	}

	public void setCode1Code2(String code1Code2) {
		this.code1Code2 = code1Code2;
	}

	public String getNextHigherAccumulationLevel() {
		return nextHigherAccumulationLevel;
	}

	public void setNextHigherAccumulationLevel(String nextHigherAccumulationLevel) {
		this.nextHigherAccumulationLevel = nextHigherAccumulationLevel;
	}

	public String getAutoDiscountItemDiscPriceOverrideMixMatch() {
		return autoDiscountItemDiscPriceOverrideMixMatch;
	}

	public void setAutoDiscountItemDiscPriceOverrideMixMatch(String autoDiscountItemDiscPriceOverrideMixMatch) {
		this.autoDiscountItemDiscPriceOverrideMixMatch = autoDiscountItemDiscPriceOverrideMixMatch;
	}

}