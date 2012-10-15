package cn.kk.extractor.lingoes;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		
		Main m = new Main();
		m.run();
		
	}
	
	private void run() {
		
		final String input = "./data/Vicon English-Chinese(S) Dictionary.ld2";
		final String output = "./data/output.txt";
		
		try {
			
			LingoesLd2Extractor extractor = new LingoesLd2Extractor(this);
			
			extractor.extractLd2ToFile(new File(input), new File(output));
			
		} catch (final Throwable t) {
			
		}		
	}

	public void setStatusDirect(int counter, int counter2, int calcSpeed) {
		// TODO Auto-generated method stub
		
	}

	public void setStatus(int defTotal, int i, int j) {
		// TODO Auto-generated method stub
		
	}

	public void setExample(int i, String cut) {
		// TODO Auto-generated method stub
		
	}

	
}
