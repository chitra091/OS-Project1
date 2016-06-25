package utdallas.cs5348.project1;

import java.util.ArrayList;
import java.util.HashMap;

public class Batch {

	private String workingDir;
	private ArrayList<Command> commands = new ArrayList<Command>();
	private HashMap<String, String> fileMap = new HashMap<String, String>();

	public String getWorkingDir() {
		return this.workingDir;
	}
	
	public void setWorkingDir(String newDir) {
		this.workingDir = newDir;
	}
	
	public void addCommand(Command cmd) {
		commands.add(cmd);
	}
	
	public ArrayList<Command> getCommands() {
		return this.commands;
	}
	
	public void addToFileMap(String id, String path) {
		this.fileMap.put(id, path);
	}
	
	public String lookupFileMap(String id) throws ProcessException {
		if (!this.fileMap.containsKey(id)) {
			throw new ProcessException(id + " is not a valid File ID");
		}
		return this.fileMap.get(id);
	}
	
	
}
