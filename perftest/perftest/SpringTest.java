package perftest;

import gflogger.LogFactory;
import gflogger.Logger;

import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * SpringTest
 *
 * @author Vladimir Dolzhenko, vladimir.dolzhenko@gmail.com
 */
public class SpringTest {

	public static void main(String[] args) {
	    final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:app-context.xml");
	    ctx.start();

	    final Logger logger = LogFactory.getLog("com.db.fxpricing.Logger");

	    logger.info().append("test1").commit();

	    logger.debug().append("testD").commit();

	    logger.error().append("testE").commit();

	    logger.info().append("test2").commit();

	    logger.info().append("test3").commit();

	    final Logger logger2 = LogFactory.getLog("org.spring");

	    logger2.info().append("org.spring.info").commit();

	    logger2.debug().append("org.spring.debug").commit();

	    logger2.error().append("org.spring.error").commit();

	    ctx.stop();
    }
}
