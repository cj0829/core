package org.csr.core.util.io;
 
public class ByteBuffer {

	protected static final int INITIALSIZE = 1024;

	protected int used = 0;
	protected int size = 0;
	protected byte[] buff = null;

	/**
	 * Initializes a new ByteBuffer object and creates
	 * a temporary buffer array of a predefined initial size.
	 * If you want to set your own initial size, use the <code>setSize</code>
	 * method after initializing the object.
	 * 
	 */
	public ByteBuffer()
	{
		this(INITIALSIZE);

	}
	/**
	 * Initializes a new ByteBuffer object and creates
	 * a temporary buffer array of a predefined initial size.
	 * If you want to set your own initial size, use the <code>setSize</code>
	 * method after initializing the object.
	 * 
	 */
	public ByteBuffer(int initSize)
	{
		size = initSize;
		buff = new byte[size];
		used=0;
	}

	/**
	 * Appends a byte to the end of the buffer
	 *
	 * If the currently reserved memory is used, the size of the 
	 * internal buffer will be doubled.
	 * In this case the memory usage will temprary increase by factor 3
	 * because it need a temporary storage for the old data.
	 *
	 * Be sure that you have enough heap memory !
	 *
	 * @param b byte to append
	 */
	public void append(byte b)
	{
		if (used >= size)
		{
			doubleBuffer();
		}

		buff[used] = b;
		used++;
	}
	public void append(byte[] b)
	{
		append(b, 0, b.length);

	}
	public void append(byte[] b, int start, int len)
	{
		for (int i = start; i < len; i++)
		{
			append(b[i]);
		}

	}

	/**
	 * @return the number of bytes stored in the buffer
	 */
	public int length() {
		return used;
	}

	/**
	 * @return the buffer contents as a byte array
	 */
	public byte[] getContent()
	{
		byte[] b = new byte[used];
		System.arraycopy(buff, 0, b, 0, used);
		return b;
	}

	/**
	 * removes all contents in the buffer
	 */
	public void clean() {
		used = 0;
	}

	/**
	 * Sets the size of the internal buffer to
	 * the given value. This is useful, if the size of the
	 * data that should be stored is known.
	 * @param size size of the buffer in Bytes
	 */
	public void setSize(int newSize)
	{

		// if we have already used more data, ignore it !
		if (newSize < used)
		{
			return;
		}

		this.size = newSize;
		byte[] newBuff = new byte[size];
		System.arraycopy(buff, 0, newBuff, 0, used);
		this.buff=newBuff;

	}

	/**
	 * Print the buffer content as a String (use it for debugging only !)
	 * @return a String containing every byte in the buffer as a character
	 */
	public String toString()
	{

		return new String(buff, 0, used);
	}

	/**
	 * doubles the size of the internal buffer
	 */
	protected void doubleBuffer() {
		// increase size
		setSize(size * 2);
	}

	/**
	 * @see Object#finalize()
	 */
	protected void finalize() throws Throwable
	{
		super.finalize();
		buff=null;
	}

}