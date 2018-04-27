package com.nordicpeak.flowengine.search;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.util.Version;


public class CaseInsensitiveWhitespaceAnalyzer extends Analyzer {
    
	private final Version matchVersion;
	
	public CaseInsensitiveWhitespaceAnalyzer(Version matchVersion) {

		this.matchVersion = matchVersion;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {

		Tokenizer tokenizer = new WhitespaceTokenizer(matchVersion, reader);
        TokenStream filter = new LowerCaseFilter(matchVersion, tokenizer);
        return new TokenStreamComponents(tokenizer, filter);
	}	
}
