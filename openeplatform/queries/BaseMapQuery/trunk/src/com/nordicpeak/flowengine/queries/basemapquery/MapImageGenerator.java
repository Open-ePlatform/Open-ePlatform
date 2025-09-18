package com.nordicpeak.flowengine.queries.basemapquery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.rowset.serial.SerialException;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.log4jutils.levels.LogLevel;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;

import com.nordicpeak.flowengine.interfaces.QueryInstance;

public class MapImageGenerator {

	private String printServiceAddress;

	private Integer mapFishConnectionTimeout;

	private Integer mapFishReadTimeout;

	private LogLevel fishTimeoutLogLevel;

	private LogLevel fishErrorLogLevel;

	private QueryInstance queryInstance;

	private User user;

	private List<GenerateMapImageTask> tasks;

	//private ExecutorService executor;

	private ScheduledExecutorService executor;

	public MapImageGenerator(String printServiceAddress, Integer mapFishConnectionTimeout, Integer mapFishReadTimeout, LogLevel fishTimeoutLogLevel, LogLevel fishErrorLogLevel, QueryInstance queryInstance, User user) {

		this.printServiceAddress = printServiceAddress;
		this.mapFishConnectionTimeout = mapFishConnectionTimeout;
		this.mapFishReadTimeout = mapFishReadTimeout;
		this.fishTimeoutLogLevel = fishTimeoutLogLevel;
		this.fishErrorLogLevel = fishErrorLogLevel;
		this.queryInstance = queryInstance;
		this.user = user;
		this.tasks = new ArrayList<GenerateMapImageTask>();
	}

	public GenerateMapImageTask addMapImageTask(String printConfig) {

		GenerateMapImageTask task = new GenerateMapImageTask(printConfig, this);

		tasks.add(task);

		return task;
	}

	public void generateMapImages(boolean async, int startDelay) throws ValidationException {

		if (tasks.size() == 0) {
			return;
		}

		try {

			if (async) {

				//executor = Executors.newFixedThreadPool(tasks.size());
				executor = Executors.newScheduledThreadPool(tasks.size());

				int delay = 0;

				for (GenerateMapImageTask task : tasks) {

					//executor.execute(task);
					executor.schedule(task, delay, TimeUnit.MILLISECONDS);

					delay += startDelay;
				}

				executor.shutdown();

				while (!executor.isTerminated()) {}

				for (GenerateMapImageTask task : tasks) {

					if (task.getGeneratedMapImage() == null || task.getGeneratedMapImage().length() == 0) {

						throw new ValidationException(new ValidationError("UnableToGeneratePNG"));
					}
				}

			} else {

				for (GenerateMapImageTask task : tasks) {

					task.run();

					if (task.getGeneratedMapImage() == null || task.getGeneratedMapImage().length() == 0) {

						throw new ValidationException(new ValidationError("UnableToGeneratePNG"));
					}
				}

			}

		} catch (SerialException serialException) {

			throw new ValidationException(new ValidationError("UnableToGeneratePNG"));
		}

	}

	public String getPrintServiceAddress() {

		return printServiceAddress;
	}

	public Integer getMapFishConnectionTimeout() {

		return mapFishConnectionTimeout;
	}

	public Integer getMapFishReadTimeout() {

		return mapFishReadTimeout;
	}

	public LogLevel getFishTimeoutLogLevel() {

		return fishTimeoutLogLevel;
	}

	public LogLevel getFishErrorLogLevel() {

		return fishErrorLogLevel;
	}

	public QueryInstance getQueryInstance() {

		return queryInstance;
	}

	public User getUser() {

		return user;
	}

}
