package org.csr.core.util.io;

public class ImportExportType {
	public static final String MODEL_MIMETYPE = "text/xml";
	public static final String MODEL_SUFFIX = ".xml";
	private String id;
	private boolean model;
	private String mimeType;

	public String toString() {
		return "<id=" + this.id + "; model=" + this.model + "; type="
				+ this.mimeType + ">";
	}

	public ImportExportType(String id, boolean model, String mimeType) {
		this.id = id;
		this.model = model;
		if (model)
			mimeType = "text/xml";
		else
			this.mimeType = mimeType;
	}

	public String getId() {
		return this.id;
	}

	public boolean isModel() {
		return this.model;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public boolean equals(Object o) {
		if ((o instanceof ImportExportType)) {
			ImportExportType iet = (ImportExportType) o;
			boolean idEqual = stringEqual(iet.getId(), this.id);
			boolean mimeEqual = stringEqual(iet.getMimeType(), this.mimeType);
			return (idEqual) && (mimeEqual);
		}
		return false;
	}

	public boolean equals(String id, String mimeType) {
		boolean idEqual = stringEqual(this.id, id);
		boolean mimeEqual = stringEqual(this.mimeType, mimeType);
		return (idEqual) && (mimeEqual);
	}

	public int hashCode() {
		return this.id.hashCode() * this.mimeType.hashCode();
	}

	private boolean stringEqual(String str1, String str2) {
		return str1 == null ? false : str2 == null ? true : str1.equals(str2);
	}
}
