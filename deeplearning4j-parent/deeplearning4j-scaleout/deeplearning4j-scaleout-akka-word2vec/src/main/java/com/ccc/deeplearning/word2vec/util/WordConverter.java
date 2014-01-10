package com.ccc.deeplearning.word2vec.util;

import java.util.ArrayList;
import java.util.List;

import org.jblas.DoubleMatrix;

import com.ccc.deeplearning.util.MatrixUtil;
import com.ccc.deeplearning.word2vec.Word2Vec;

public class WordConverter {

	private List<String> sentences = new ArrayList<String>();
	private Word2Vec vec;
	private List<Window> windows;

	public WordConverter(List<String> sentences,Word2Vec vec) {
		this.sentences = sentences;
		this.vec = vec;
	}

	public DoubleMatrix toInputMatrix() {
		List<Window> windows = allWindowsForAllSentences();
		int columns = vec.getLayerSize() * vec.getWindow();
		int rows = windows.size();
		DoubleMatrix ret = new DoubleMatrix(rows,columns);
		for(int i = 0; i < rows; i++) {
			ret.putRow(i,MatrixUtil.normalize(new DoubleMatrix(WindowConverter.asExample(windows.get(i), vec))));
		}
		return ret;
	}

	public DoubleMatrix toLabelMatrix(List<String> labels) {
		int columns = labels.size();
		List<Window> windows = allWindowsForAllSentences();
		DoubleMatrix ret = new DoubleMatrix(windows.size(),columns);
		for(int i = 0; i < ret.rows; i++) {
			ret.putRow(i,MatrixUtil.toOutcomeVector(labels.indexOf(windows.get(i).getLabel()), labels.size()));
		}
		return ret;
	}

	private List<Window> allWindowsForAllSentences() {
		if(windows != null)
			return windows;
		windows = new ArrayList<Window>();
		for(String s : sentences)
			if(!s.isEmpty())
				windows.addAll(Windows.windows(s));
		return windows;
	}



}