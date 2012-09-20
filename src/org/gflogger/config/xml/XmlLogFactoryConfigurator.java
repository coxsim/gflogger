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

package org.gflogger.config.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.gflogger.GFLogFactory;

/**
 *
 * @author Harald Wendel
 * @author Vladimir Dolzhenko, vladimir.dolzhenko@gmail.com
 */
public final class XmlLogFactoryConfigurator {

	public static void configure(InputStream in) throws Exception {
		new XmlLogFactoryConfigurator(in);
	}

	public static void configure(final String xmlFileName) throws Exception {
		configure(XmlLogFactoryConfigurator.class.getResourceAsStream(xmlFileName));
	}

	public static void configure() throws Exception {
		configure(System.getProperty("gflogger.configuration", "/gflogger.xml"));
	}

	private XmlLogFactoryConfigurator(final InputStream in) throws Exception {
		if (in == null) throw new IllegalArgumentException("Not a null input stream is expected.");
		try {
			final SAXParserFactory factory = SAXParserFactory.newInstance();
			final SAXParser saxParser;

			final InputStream is = getClass().getResourceAsStream("/gflogger.xsd");

			if (is != null){
				factory.setNamespaceAware(true);
				factory.setValidating(true);

				saxParser = factory.newSAXParser();
				saxParser.setProperty( "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");
				saxParser.setProperty( "http://java.sun.com/xml/jaxp/properties/schemaSource",
					is);
			} else {
				saxParser = factory.newSAXParser();
			}

			final Configuration configuration = new Configuration();
			saxParser.parse(in, configuration);

			GFLogFactory.init(configuration.getLoggerService());
		} finally {
			in.close();
		}
	}
}
