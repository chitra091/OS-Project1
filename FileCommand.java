package utdallas.cs5348.project1;

import org.w3c.dom.Element;


public class FileCommand extends Command {
	private String path;
	
	public FileCommand(Batch batch) {
		super(batch);
	}

	@Override
	public String describe() {
		return "File Command on file: " + path;
	}

	@Override
	public void execute(String workingDir) {
		this.batch.addToFileMap(id, path);
	}

	@Override
	public void parse(Element element) throws ProcessException {

		this.id = element.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in CMD Command");
		}
				
		this.path = element.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in CMD Command");
		}
	}

}
