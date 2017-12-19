package com.nordicpeak.flowengine.formatvalidation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.NameComparator;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.webutils.http.URIParser;


public class FormatValidationHandlerModule extends AnnotatedForegroundModule implements FormatValidationHandler {

	private final ConcurrentHashMap<String, FormatValidator> validatorMap = new ConcurrentHashMap<String, FormatValidator>();
	
	private List<FormatValidator> sortedValidatorList;
	
	private List<SimpleFormatValidator> cachedLocalValidators;
	
	private AnnotatedDAO<SimpleFormatValidator> formatValidatorDAO;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if(!systemInterface.getInstanceHandler().addInstance(FormatValidationHandler.class, this)){

			throw new RuntimeException("Unable to register module in global instance handler using key " + FormatValidationHandler.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(FormatValidationHandler.class, this);

		this.validatorMap.clear();
		this.sortedValidatorList.clear();
		this.cachedLocalValidators = null;
		
		super.unload();
	}	
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FormatValidationHandlerModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}
		
		formatValidatorDAO = new SimpleAnnotatedDAOFactory(dataSource).getDAO(SimpleFormatValidator.class);
		
		if(upgradeResult.getInitialVersion() < 2){
			
			setPlaceholderTexts();
		}
		
		loadLocalValidators();
		
		super.createDAOs(dataSource);
	}
	
	private void setPlaceholderTexts() throws SQLException {

		List<SimpleFormatValidator> dbValidators = formatValidatorDAO.getAll();
		
		if(dbValidators != null){
			
			for(SimpleFormatValidator formatValidator : dbValidators){
				
				if(formatValidator.getPlaceholder() == null && formatValidator.getName().contains("(") && formatValidator.getName().contains(")")) {
					
					String placeholder = StringUtils.substringAfter(formatValidator.getName(), "(");
					
					if(!StringUtils.isEmpty(placeholder) && placeholder.contains(")")){
						
						placeholder = StringUtils.substringBefore(placeholder, ")");
						
						if(!StringUtils.isEmpty(placeholder)){
							
							formatValidator.setPlaceholder(placeholder);
							
							log.info("Updating placeholder of format validator " + formatValidator);
							
							formatValidatorDAO.update(formatValidator);
						}
					}
				}
			}
		}
	}

	public synchronized void loadLocalValidators() throws SQLException{
		
		List<SimpleFormatValidator> dbValidators = formatValidatorDAO.getAll();
		
		if(dbValidators != null){
			
			Iterator<SimpleFormatValidator> validatorIterator = dbValidators.iterator();
			
			while(validatorIterator.hasNext()){
				
				SimpleFormatValidator formatValidator = validatorIterator.next();
				
				try{
					formatValidator.init();
					
				}catch(Throwable t){
					
					log.error("Unable to initialize format validator " + formatValidator, t);
					
					validatorIterator.remove();
				}
			}
		}
		
		if(cachedLocalValidators != null){
			
			for(SimpleFormatValidator cachedLocalFormatValidator : dbValidators){
				
				validatorMap.remove(cachedLocalFormatValidator.getClassName());
			}
			
			log.info("Removed " + cachedLocalValidators.size() + " local validators");
		}
		
		if(!CollectionUtils.isEmpty(dbValidators)){
			
			for(SimpleFormatValidator formatValidator : dbValidators){
				
				FormatValidator replacedValidator = validatorMap.put(formatValidator.getClassName(), formatValidator);
				
				if(replacedValidator != null){
					
					log.warn("Format validator " + replacedValidator + " has been replaced with local validator " + formatValidator);
				}
			}
			
			cachedLocalValidators = dbValidators;
			
			log.info("Added " + dbValidators.size() + " local validators");
			
		}else{
			
			cachedLocalValidators = null;
		}
		
		generateSortedList();
	}
	
	protected synchronized void generateSortedList(){
		
		ArrayList<FormatValidator> formatValidators = new ArrayList<FormatValidator>(validatorMap.values());
		
		Collections.sort(formatValidators, NameComparator.getInstance());
		
		sortedValidatorList = formatValidators;
	}
	
	@Override
	public List<FormatValidator> getFormatValidators() {

		return sortedValidatorList;
	}

	@Override
	public FormatValidator getFormatValidator(String className) {

		return validatorMap.get(className);
	}

	@Override
	public boolean addFormatValidator(FormatValidator formatValidator) {

		if(formatValidator == null){
			
			throw new NullPointerException("Format validator cannot be null");
		}
		
		boolean added = validatorMap.putIfAbsent(formatValidator.getClassName(), formatValidator) == null;
		
		if(added){
			
			generateSortedList();
		}
		
		return added;
	}

	@Override
	public boolean removeFormatValidator(FormatValidator formatValidator) {

		if(formatValidator == null){
			
			throw new NullPointerException("Format validator cannot be null");
		}
		
		boolean removed = validatorMap.remove(formatValidator) != null;
		
		if(removed){
			
			generateSortedList();
		}
		
		return removed;
	}

	@WebPublic
	public ForegroundModuleResponse reload(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		loadLocalValidators();
		
		return null;
	}
}
