package webFrontCommonUtils;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ACT {

	@SerializedName("lines")
	@Expose
	private List<ACTLine> lines = null;

	public List<ACTLine> getLines() {
		return lines;
	}

	public void setLines(List<ACTLine> lines) {
		this.lines = lines;
	}

}