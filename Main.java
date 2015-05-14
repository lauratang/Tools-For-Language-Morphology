import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.*;

public class Main {

	private static FLEx flex;

	public static void main(String[] args) {		
		
		/**
		 * GUI Setup
		 */

		flex = new FLEx();
		flex.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		flex.setMinimumSize(new Dimension(1000,400));
		
		// Auto-save on close
		flex.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	// writing file
				try (BufferedWriter writer = Files.newBufferedWriter(
						Paths.get("lexicon.txt"), Charset.forName("UTF-8"))) {
					JTable table1 = flex.getTable();
					for (int i = 0; i < table1.getRowCount(); i++) {
						LexiconObject x = (LexiconObject) table1.getModel().getValueAt(
								i, 0);
						writer.write(x.getLexemeForm() + "\t" + x.getLexemeFormIPA() + "\t"
								+ x.getMorphemeType() + "\t" + x.getComplexFormType()
								+ "\t" + x.getGloss() + "\t" + x.getCategory());
						// write multiple senses
						int start = 2;
						int stop = x.getSenseCount();
						while (start <= stop) {
							writer.write("\t" + x.getGloss(start) + "\t"
									+ x.getCategory(start));
							start++;
						}
						writer.newLine();
					}

				} catch (IOException ex) {
					ex.printStackTrace();
				}
		    }
		});
		
		flex.pack();
		flex.setVisible(true);

	}
}
