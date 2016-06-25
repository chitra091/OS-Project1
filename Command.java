package utdallas.cs5348.project1;

import org.w3c.dom.Element;

public abstract class Command {
	
	protected Batch batch;
	protected String id;
	
	public Command(Batch batch) {
		this.batch = batch;
	}
	
	public String getID() {
		return id;
	}
	
	abstract public String describe();
	
	abstract public void execute(String workingDir) throws ProcessException;
	
	abstract public void parse(Element element) throws ProcessException;
		
}