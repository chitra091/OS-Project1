package utdallas.cs5348.project1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PipeCommand extends Command {

	private List<PipeCmdCommand> commands = new ArrayList<PipeCmdCommand>();
	
	public PipeCommand(Batch batch) {
		super(batch);
	}

	@Override
	public String describe() {
		return "Pipe Command";
	}

	@Override
	public void execute(String workingDir) throws ProcessException {
		
		List<String> command1 = commands.get(0).getCommand();
		List<String> command2 = commands.get(1).getCommand();
		
		ProcessBuilder builder1 = new ProcessBuilder(command1);
		ProcessBuilder builder2 = new ProcessBuilder(command2);
		
		builder1.directory(new File(workingDir));
		builder2.directory(new File(workingDir));
		
		builder1.redirectInput(new File(workingDir, commands.get(0).getInputPath()));
		builder2.redirectOutput(new File(workingDir, commands.get(1).getOutputPath()));
		
		try {
			final Process process1 = builder1.start();
			final Process process2 = builder2.start();
			
			InputStream is = process1.getInputStream();
			OutputStream os = process2.getOutputStream();
			
			System.out.println("Waiting for " + commands.get(0).getID() + " to exit");
			
			int achar;
			while((achar = is.read()) != -1) {
				os.write(achar);
			}
			is.close();
			os.close();
			
			process1.waitFor();
			System.out.println(commands.get(0).getID() + " has exited");
			System.out.println("Waiting for " + commands.get(1).getID() + " to exit");
			process2.waitFor();
			System.out.println(commands.get(1).getID() + " has exited");
			
		} catch (IOException e) {
			throw new ProcessException("PipeCommand failed during execution.");
		} catch (InterruptedException e) {
			throw new ProcessException("PipeCommand failed during execution.");
		}	
	}

	@Override
	public void parse(Element element) throws ProcessException {
		id = element.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in PIPE Command");
		}
		
		NodeList nodes = element.getElementsByTagName("*");
		
		if(nodes.getLength() != 2) {
			throw new ProcessException("Invalid number of child elements of PIPE Command");
		}
		
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			Node node = nodes.item(idx);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				
				String cmdName = elem.getNodeName();
				if (!"cmd".equalsIgnoreCase(cmdName)) {
					throw new ProcessException("Only CMD Command allowed within a PIPE");
				}
				
				PipeCmdCommand cmd = new PipeCmdCommand(batch);
				cmd.parse(elem);
				commands.add(cmd);
			}
		}

		PipeCmdCommand command = commands.get(0);
		if(command.getOutID() != "") {
			throw new ProcessException("First CMD Command within PIPE should have no output attribute");
		}
	
		command = commands.get(1);
		if(command.getInID() != "") {
			throw new ProcessException("Second CMD Command within PIPE should no input attribute");
		}		
	}
}
