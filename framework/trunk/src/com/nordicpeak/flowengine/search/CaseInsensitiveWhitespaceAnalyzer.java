package com.nordicpeak.flowengine.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;


public class CaseInsensitiveWhitespaceAnalyzer extends Analyzer {
    
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {

		Tokenizer tokenizer = new WhitespaceTokenizer();
        TokenStream filter = new LowerCaseFilter(tokenizer);
        return new TokenStreamComponents(tokenizer, filter);
	}	
}