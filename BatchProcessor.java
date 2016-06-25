package utdallas.cs5348.project1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class BatchProcessor {

	public static void main(String[] args) throws ProcessException {
		
		String filename;
		ArrayList<Batch> batchesToExecute = new ArrayList<Batch>();
		
		if (args.length == 0) {
			throw new ProcessException("No filename passed into BatchProcessor.");
		}
		filename = args[0];

		try {
			File f = new File(filename);
			Batch batch = BatchParser.buildBatch(f);
			batchesToExecute.add(batch);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new ProcessException("Error reading batch file.");
		} catch (ProcessException e) {
			e.printStackTrace();
		}
		
		System.out.println("Executing batches");
		for(Batch batch : batchesToExecute) {
			try {
				executeBatch(batch);
			} catch (ProcessException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Finished running batches");
		
	}

	static void executeBatch(Batch batch) throws ProcessException {
		for(Command cmd : batch.getCommands()) {
			System.out.println(cmd.describe());
			cmd.execute(batch.getWorkingDir());
		}
	}
}
