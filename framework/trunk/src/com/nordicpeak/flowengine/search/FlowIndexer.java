package com.nordicpeak.flowengine.search;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.SessionUtils;

import com.nordicpeak.flowengine.beans.Flow;


public class FlowIndexer {

	private static final String ID_FIELD = "id";
	private static final String NAME_FIELD = "name";
	private static final String SHORT_DESCRIPTION_FIELD = "short-description";
	private static final String LONG_DESCRIPTION_FIELD = "long-description";
	private static final String TAGS_FIELD = "tag";
	private static final String CATEGORY_FIELD = "category";

	private static final String[] SEARCH_FIELDS = new String[]{NAME_FIELD, SHORT_DESCRIPTION_FIELD, LONG_DESCRIPTION_FIELD, TAGS_FIELD, CATEGORY_FIELD};

	protected Logger log = Logger.getLogger(this.getClass());

	private final CaseInsensitiveWhitespaceAnalyzer analyzer = new CaseInsensitiveWhitespaceAnalyzer(Version.LUCENE_44);
	private final Directory index = new RAMDirectory();
	private IndexWriter indexWriter;
	private IndexReader indexReader;
	private IndexSearcher searcher;

	protected int maxHitCount;

	public FlowIndexer(Collection<Flow> flows, int maxHitCount) throws IOException{

		this.maxHitCount = maxHitCount;

		HtmlParser htmlParser = new HtmlParser();

		indexWriter = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_44, analyzer));

		for(Flow flow : flows){

			try{
				Document doc = new Document();
				
				doc.add(new IntField(ID_FIELD, flow.getFlowID(), Field.Store.YES));
				doc.add(new TextField(NAME_FIELD, flow.getName(), Field.Store.NO));
				doc.add(new TextField(SHORT_DESCRIPTION_FIELD, parseHTML(flow.getShortDescription(), htmlParser), Field.Store.NO));

				if(!StringUtils.isEmpty(flow.getLongDescription())){
					
					doc.add(new TextField(LONG_DESCRIPTION_FIELD, parseHTML(flow.getLongDescription(), htmlParser), Field.Store.NO));
				}

				if(flow.getTags() != null){

					for(String tag : flow.getTags()){

						doc.add(new TextField(TAGS_FIELD, tag, Field.Store.NO));
					}
				}

				if(flow.getCategory() != null){

					doc.add(new TextField(CATEGORY_FIELD, flow.getCategory().getName(), Field.Store.NO));
				}

				indexWriter.addDocument(doc);

			}catch(Exception e){

				log.error("Error indexing flow " + flow, e);
			}
		}

		this.indexWriter.commit();
		this.indexReader = DirectoryReader.open(index);
		this.searcher = new IndexSearcher(indexReader);
	}

	public void close(){

		try {
			indexWriter.close();
		} catch (IOException e) {
			log.warn("Error closing index writer", e);
		}

		try {
			index.close();
		} catch (IOException e) {
			log.warn("Error closing index", e);
		}
	}

	private String parseHTML(String text, HtmlParser htmlParser) throws IOException, SAXException, TikaException {

		StringWriter writer = new StringWriter();
		ContentHandler contentHandler = new BodyContentHandler(writer);

		Metadata metadata = new Metadata();
		metadata.set(Metadata.CONTENT_TYPE, "text/html");

		htmlParser.parse(StringUtils.getInputStream(text), contentHandler, metadata, new ParseContext());

		return writer.toString();
	}

	public void search(HttpServletRequest req, HttpServletResponse res, User user) throws IOException{

		String queryString = req.getParameter("q");

		log.info("User " + user + " searching for: " + StringUtils.toLogFormat(queryString, 50));

		if (StringUtils.isEmpty(queryString)) {

			resetLastSearch(req, user);
			sendEmptyResponse(res);
			return;
		}
		
		queryString = URLDecoder.decode(queryString, "UTF-8");
		//queryString = queryString.replace(":", " ");
		queryString = queryString.trim();
		
		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_44, SEARCH_FIELDS, analyzer);

		Query query;

		try {
			query = parser.parse(QueryParser.escape(queryString) + "*");
			SessionUtils.setAttribute("lastsearch", queryString, req);

		} catch (ParseException e) {

			log.warn("Unable to parse query string " + StringUtils.toLogFormat(queryString, 50) + " requsted by user " + user + " accessing from " + req.getRemoteAddr());

			sendEmptyResponse(res);
			return;
		}

		TopDocs results = searcher.search(query, maxHitCount);

		if (results.scoreDocs.length == 0) {

			sendEmptyResponse(res);
			return;
		}

		JsonArray hitsArray = new JsonArray(results.scoreDocs.length);

		//Create JSON from hits
		for(ScoreDoc scoreDoc : results.scoreDocs){

			Document doc = searcher.doc(scoreDoc.doc);

			hitsArray.addNode(doc.get(ID_FIELD));
		}

		JsonObject jsonObject = new JsonObject(2);
		jsonObject.putField("hitCount", Integer.toString(results.scoreDocs.length));
		jsonObject.putField("hits", hitsArray);
		
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
		return;
	}
	
	public void resetLastSearch(HttpServletRequest req, User user) throws IOException{
		
		SessionUtils.removeAttribute("lastsearch", req);
	}

	
	public List<Integer> search(String queryString) throws IOException{
		
		if (StringUtils.isEmpty(queryString)) {

			return null;
		}

		//queryString = queryString.replace(":", " ");
		queryString = queryString.trim();
		
		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_44, SEARCH_FIELDS, analyzer);

		Query query;

		try {
			query = parser.parse(QueryParser.escape(queryString) + "*");

		} catch (ParseException e) {

			log.warn("Unable to parse query string " + StringUtils.toLogFormat(queryString, 50));

			return null;
		}

		TopDocs results = searcher.search(query, maxHitCount);

		if (results.scoreDocs.length == 0) {

			return null;
		}
		
		ArrayList<Integer> hits = new ArrayList<Integer>(results.scoreDocs.length);
		
		for(ScoreDoc scoreDoc : results.scoreDocs){

			Document doc = searcher.doc(scoreDoc.doc);

			hits.add(Integer.valueOf(doc.get(ID_FIELD)));
		}
		
		return hits;
	}

	public static void sendEmptyResponse(HttpServletResponse res) throws IOException {

		JsonObject jsonObject = new JsonObject(1);
		jsonObject.putField("hitCount", "0");
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
	}
}
