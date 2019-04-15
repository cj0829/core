package org.csr.core.util.mm;

import org.csr.core.util.mm.util.EncodingGuesser;

/**
 * This class can be used to represent a mime type for a text file.
 * This should only be returned by MimeDetector(s) that use magic number
 * type matching. It allows for an encoding to be associated to a text type
 * mime type such as text/plain.
 *
 * @author Steven McArdle
 *
 */
public class TextMimeType extends MimeType {

	private static final long serialVersionUID = -4798584119063522367L;

	// The default encoding is always set Unknown
	private String encoding = "Unknown";

	/**
	 * Construct a TextMimeType from a string representation of a MimeType and
	 * an encoding that should be one of the known encodings.
	 *
	 * @param mimeType
	 * @param encoding
	 *
	 * @see #getKnownEncodings()
	 * @see #addKnownEncoding(String)
	 */
	public TextMimeType(final String mimeType, final String encoding) {
		super(mimeType);
		this.encoding = getValidEncoding(encoding);
	}

	public TextMimeType(final MimeType mimeType, final String encoding) {
		super(mimeType);
		this.encoding = getValidEncoding(encoding);
	}

	public TextMimeType(final MimeType mimeType) {
		super(mimeType);
		// We don't change the encoding
	}

	public void setMimeType(MimeType mimeType) {
		mediaType = mimeType.mediaType;
		subType = mimeType.subType;
	}

	/**
	 * Get the encoding currently set for this TextMimeType.
	 * @return the encoding as a string
	 * @see #getKnownEncodings()
	 * @see #setEncoding(String)
	 */
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String toString() {
		return super.toString() + ";charset=" + getEncoding();
	}

	/**
	 * Utility method to see if the passed in encoding is known to this class.
	 *
	 * @param encoding
	 * @return true if encoding passed in is one of the known encodings else false
	 * @see #getKnownEncodings()
	 */
	private boolean isKnownEncoding(String encoding) {
		return EncodingGuesser.isKnownEncoding(encoding);
	}

	private String getValidEncoding(String encoding) {
		if(isKnownEncoding(encoding)) {
			return encoding;
		} else {
			return "Unknown";
		}
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}
}

