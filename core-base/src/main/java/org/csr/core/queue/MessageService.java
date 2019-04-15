package org.csr.core.queue;



public interface MessageService<T> {

	void processMessages(Message<T> messages);
}
