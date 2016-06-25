package utdallas.cs5348.project1;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.List;

import org.w3c.dom.Element;

public class PipeCmdCommand extends Command {

	private String path;
	private List<String> cmdArgs;
	private String inID;
	private String outID;
	
	public PipeCmdCommand(Batch batch) {
		super(batch);
	}

	@Override
	public String describe() {
		return "Pipe Command";
	}

	@Override
	public void execute(String workingDir) throws ProcessException {
		
	}

	@Override
	public void parse(Element element) throws ProcessException {
		id = element.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in CMD Command");
		}
		
		path = element.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in CMD Command");
		}
		
		// Arguments must be passed to ProcessBuilder as a list of
		// individual strings. 
		cmdArgs = new ArrayList<String>();
		String arg = element.getAttribute("args");
		if (!(arg == null || arg.isEmpty())) {
			StringTokenizer st = new StringTokenizer(arg);
			while (st.hasMoreTokens()) {
				String tok = st.nextToken();
				cmdArgs.add(tok);
			}
		}

		inID = element.getAttribute("in");
		
		outID = element.getAttribute("out");
	}
	
	public List<String> getCommand() {
		List<String> command = new ArrayList<String>();
		command.add(path);
		for(String argi: cmdArgs) {
			command.add(argi);
		}
		return command;
	}
	
	public String getInputPath() throws ProcessException {
		return batch.lookupFileMap(inID);
	}
	
	public String getOutputPath() throws ProcessException {
		return batch.lookupFileMap(outID);
	}
	
	public String getOutID() {
		return outID;
	}
	
	public String getInID() {
		return inID;
	}
}
