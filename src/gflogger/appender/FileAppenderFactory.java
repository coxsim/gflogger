/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gflogger.appender;

import gflogger.Appender;
import gflogger.LoggerService;
import gflogger.base.DefaultLoggerServiceImpl;
import gflogger.disruptor.DLoggerServiceImpl;

/**
 * FileAppenderFactory
 * 
 * @author Vladimir Dolzhenko, vladimir.dolzhenko@gmail.com
 */
public class FileAppenderFactory extends AbstractAppenderFactory {

	protected String fileName;
	protected String codepage = "UTF-8";
	protected boolean append = true;

	@Override
	public Appender createAppender(Class<? extends LoggerService> loggerServiceClass) {
		if (DefaultLoggerServiceImpl.class.equals(loggerServiceClass)){
			final gflogger.base.appender.FileAppender appender = 
				new gflogger.base.appender.FileAppender(bufferSize);

			appender.setLogLevel(logLevel);
			appender.setLayout(layout);
			appender.setImmediateFlush(immediateFlush);
			appender.setBufferedIOThreshold(bufferedIOThreshold);
			appender.setAwaitTimeout(awaitTimeout);
			
			appender.setFileName(fileName);
			appender.setCodepage(codepage);
			appender.setAppend(append);
			return appender;
		} else if (DLoggerServiceImpl.class.equals(loggerServiceClass)){
			final gflogger.disruptor.appender.FileAppender appender = 
				new gflogger.disruptor.appender.FileAppender(bufferSize);

			appender.setLogLevel(logLevel);
			appender.setLayout(layout);
			appender.setImmediateFlush(immediateFlush);
			appender.setBufferedIOThreshold(bufferedIOThreshold);
			appender.setAwaitTimeout(awaitTimeout);
			
			appender.setFileName(fileName);
			appender.setCodepage(codepage);
			appender.setAppend(append);
			return appender;
		}
		throw new IllegalArgumentException(loggerServiceClass.getName() 
			+ " is unsupported type of logger service");
	}

	/*
	 * Setters'n'Getters
	 */
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCodepage() {
		return this.codepage;
	}

	public void setCodepage(String codepage) {
		this.codepage = codepage;
	}

	public boolean isAppend() {
		return this.append;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}
	
}