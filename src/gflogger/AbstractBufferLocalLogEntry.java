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
package gflogger;

import gflogger.helpers.LogLog;

import java.nio.BufferOverflowException;

/**
 * AbstractBufferLocalLogEntry
 *
 * @author Vladimir Dolzhenko, vladimir.dolzhenko@gmail.com
 */
abstract class AbstractBufferLocalLogEntry implements LocalLogEntry {

	protected final String threadName;
	protected final LoggerService loggerService;

	protected String categoryName;
	protected LogLevel logLevel;

	protected boolean commited = true;
	protected Throwable error;

	protected String pattern;
	protected int pPos;

	public AbstractBufferLocalLogEntry(final Thread owner, LoggerService loggerService) {
		/*
		 * It worth to cache thread categoryName at thread local variable cause
		 * thread.getName() creates new String(char[])
		 */
		this.threadName = owner.getName();
		this.loggerService = loggerService;
	}

	@Override
	public LogLevel getLogLevel() {
		return logLevel;
	}

	@Override
	public void setLogLevel(final LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	@Override
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String getCategoryName() {
		return categoryName;
	}

	@Override
	public String getThreadName() {
		return threadName;
	}

	@Override
	public boolean isCommited() {
		return this.commited;
	}

	@Override
	public void setCommited(boolean commited) {
		this.commited = commited;
	}

	@Override
	public Throwable getError() {
		return this.error;
	}

	@Override
	public void setPattern(String pattern) {
		if (pattern == null){
			throw new IllegalArgumentException("expected not null pattern.");
		}
		this.pattern = pattern;
		this.pPos = 0;
		appendNextPatternChank();
	}

	protected void appendNextPatternChank(){
		final int len = pattern.length();
		for(; pPos < len; pPos++){
			final char ch = pattern.charAt(pPos);
			if (ch == '%' && (pPos + 1) < len){
				if (pattern.charAt(pPos + 1) != '%') break;
				pPos++;
			}
			append(ch);
		}
		if (this.pPos == len){
			commit();
		}
	}

	protected void checkPlaceholder(){
		if (pPos + 2 >= pattern.length()){
			throw new IllegalStateException("Illegal pattern '" + pattern + "' or position " + pPos);
		}
		final char ch1 = pattern.charAt(pPos);
		final char ch2 = pattern.charAt(pPos + 1);
		if (ch1 != '%' || ch2 != 's'){
			throw new IllegalArgumentException("Illegal pattern placeholder '" + ch1 + "" + ch2 + " at " + pPos);
		}
		pPos += 2;
	}

	@Override
	public LogEntry append(Loggable loggable) {
		loggable.appendTo(this);
		return this;
	}

	@Override
	public LogEntry append(Object o) {
		try {
			if (o != null){
				append(o.toString());
			} else {
				append('n').append('u').append('l').append('l');
			}
		} catch (BufferOverflowException e){
			this.error = e;
			// there is insufficient space in this buffer
			LogLog.error("append(Object o):" + e.getMessage(), e);
		}
		return this;
	}

	@Override
	public LogEntry appendIf(boolean condition, final char c) {
		if (condition) append(c);
		return this;
	}

	@Override
	public LogEntry appendIf(boolean condition, final CharSequence csq) {
		if (condition) append(csq);
		return this;
	}

	@Override
	public LogEntry appendIf(boolean condition, final CharSequence csq, final int start, final int end) {
		if (condition) append(csq, start, end);
		return this;
	}

	@Override
	public LogEntry appendIf(boolean condition, final boolean b) {
		if (condition) append(b);
		return this;
	}

	@Override
	public LogEntry appendIf(boolean condition, final byte i) {
		if (condition) append(i);
		return this;
	}

	@Override
	public LogEntry appendIf(boolean condition, final short i) {
		if (condition) append(i);
		return this;
	}

	@Override
	public LogEntry appendIf(boolean condition, final int i) {
		if (condition) append(i);
		return this;
	}

	@Override
	public LogEntry appendIf(boolean condition, final long i) {
		if (condition) append(i);
		return this;
	}

	@Override
	public LogEntry appendIf(boolean condition, final double i, final int precision) {
		if (condition) append(i, precision);
		return this;
	}

	@Override
	public LogEntry appendIf(boolean condition, Throwable e) {
		if (condition) append(e);
		return this;
	}

	@Override
	public LogEntry appendIf(boolean condition, Loggable loggable) {
		if (condition) append(loggable);
		return this;
	}

	@Override
	public LogEntry appendIf(boolean condition, Object o) {
		if (condition) append(o);
		return this;
	}

	@Override
	public FormattedLogEntry with(char c){
		checkPlaceholder();
		append(c);
		appendNextPatternChank();
		return this;
	}

	@Override
	public FormattedLogEntry with(CharSequence csq){
		checkPlaceholder();
		append(csq);
		appendNextPatternChank();
		return this;
	}

	@Override
	public FormattedLogEntry with(CharSequence csq, int start, int end){
		checkPlaceholder();
		append(csq, start, end);
		appendNextPatternChank();
		return this;
	}

	@Override
	public FormattedLogEntry with(boolean b){
		checkPlaceholder();
		append(b);
		appendNextPatternChank();
		return this;
	}

	@Override
	public FormattedLogEntry with(byte i){
		checkPlaceholder();
		append(i);
		appendNextPatternChank();
		return this;
	}

	@Override
	public FormattedLogEntry with(short i){
		checkPlaceholder();
		append(i);
		appendNextPatternChank();
		return this;
	}

	@Override
	public FormattedLogEntry with(int i){
		checkPlaceholder();
		append(i);
		appendNextPatternChank();
		return this;
	}

	@Override
	public FormattedLogEntry with(long i){
		checkPlaceholder();
		append(i);
		appendNextPatternChank();
		return this;
	}

	@Override
	public FormattedLogEntry with(double i, int precision){
		checkPlaceholder();
		append(i, precision);
		appendNextPatternChank();
		return this;
	}

	@Override
	public FormattedLogEntry with(Throwable e){
		checkPlaceholder();
		append(e);
		appendNextPatternChank();
		return this;
	}

	@Override
	public FormattedLogEntry with(Loggable loggable){
		checkPlaceholder();
		append(loggable);
		appendNextPatternChank();
		return this;
	}

	@Override
	public FormattedLogEntry with(Object o){
		checkPlaceholder();
		append(o);
		appendNextPatternChank();
		return this;
	}

	@Override
	public void endWith(char c){
		with(c);
		commit();
	}

	@Override
	public void endWith(CharSequence csq){
		with(csq);
		commit();
	}

	@Override
	public void endWith(CharSequence csq, int start, int end){
		with(csq, start, end);
		commit();
	}

	@Override
	public void endWith(boolean b){
		with(b);
		commit();
	}

	@Override
	public void endWith(byte i){
		with(i);
		commit();
	}

	@Override
	public void  endWith(short i){
		with(i);
		commit();
	}

	@Override
	public void  endWith(int i){
		with(i);
		commit();
	}

	@Override
	public void  endWith(long i){
		with(i);
		commit();
	}

	@Override
	public void endWith(double i, int precision){
		with(i, precision);
		commit();
	}

	@Override
	public void endWith(Throwable e){
		with(e);
		commit();
	}

	@Override
	public void endWith(Loggable loggable){
		with(loggable);
		commit();
	}

	@Override
	public void endWith(Object o){
		with(o);
		commit();
	}

	@Override
	public final void commit() {
		if (commited) return;
		commit0();
		loggerService.entryFlushed(this);
		commited = true;
		pattern = null;
		error = null;
	}

	protected abstract void commit0();

}