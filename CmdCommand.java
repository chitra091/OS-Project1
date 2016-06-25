package utdallas.cs5348.project1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.w3c.dom.Element;

//import batch1test.ProcessException;

public class CmdCommand extends Command {

	private String path;
	private List<String> cmdArgs;
	private String inID;
	private String outID;
	
	public CmdCommand(Batch batch) {
		super(batch);
	}

	@Override
	public String describe() {
		return "Command: " + path;
	}

	@Override
	public void execute(String workingDir) throws ProcessException {
	
		List<String> command = new ArrayList<String>();
		command.add(path);
		for (String cmd : cmdArgs) {
			command.add(cmd);
		}
		Process process = null;
		
		ProcessBuilder builder = new ProcessBuilder();

		builder.directory(new File(workingDir));
		if ((!(inID == null || inID.isEmpty()))) {
			if ((batch.lookupFileMap(inID).length() == 0) ||
					!(new File(workingDir, this.batch.lookupFileMap(inID)).exists())) {

				throw new ProcessException("Invalid input file: " + batch.lookupFileMap(inID));
			}
			builder.redirectInput(new File(workingDir, this.batch.lookupFileMap(inID)));
		}
		
		if (!(outID == null || outID.isEmpty())) {
			if (this.batch.lookupFileMap(outID).length() == 0) {
				throw new ProcessException("Invalid output file.");
			}
			builder.redirectOutput(new File (workingDir, this.batch.lookupFileMap(outID)));
		}
		
		builder.command(command);

		try {
			process = builder.start();
			process.waitFor();
		} catch (IOException e) {
			throw new ProcessException("CmdCommand failed during execution.");
		} catch (InterruptedException e) {
			throw new ProcessException("CmdCommand failed during execution.");
		}
		
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

}

