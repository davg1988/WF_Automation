package webFrontCommonUtils;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/* Generated by http://www.jsonschema2pojo.org/ */

public class LAN {

	@SerializedName("lines")
	@Expose
	private List<LANLine> lines = null;

	public List<LANLine> getLines() {
		return lines;
	}

	public void setLines(List<LANLine> lines) {
		this.lines = lines;
	}

}