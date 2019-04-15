package org.csr.core.util.mm.detector;

import java.util.List;

/**
 * This exception is thrown if while parsing the magic.mime files an invalid un-parsable entry is found
 * @author Steven McArdle
 */
class InvalidMagicMimeEntryException extends RuntimeException {

	private static final long serialVersionUID = -6705937358834408523L;

	public InvalidMagicMimeEntryException() {
        super("Invalid Magic Mime Entry: Unknown entry");
    }

    public InvalidMagicMimeEntryException(List mimeMagicEntry) {
    	super("Invalid Magic Mime Entry: " + mimeMagicEntry.toString());
    }

    public InvalidMagicMimeEntryException(List mimeMagicEntry, Throwable t) {
    	super("Invalid Magic Mime Entry: " + mimeMagicEntry.toString(), t);
    }

    public InvalidMagicMimeEntryException(Throwable t) {
    	super(t);
    }

    public InvalidMagicMimeEntryException(String message, Throwable t) {
    	super(message, t);
    }
}
