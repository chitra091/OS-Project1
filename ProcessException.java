package utdallas.cs5348.project1;

@SuppressWarnings("serial")
public class ProcessException extends Exception {
	
	public ProcessException(String message) {
		super(message);
	}
	
	public ProcessException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
