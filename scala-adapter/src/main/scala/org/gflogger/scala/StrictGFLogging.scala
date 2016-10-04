package org.gflogger.scala

import org.gflogger.GFLogFactory

/**
  * Created by simon on 1/10/2016.
  */
trait StrictGFLogging {
  val logger = GFLog(GFLogFactory.getLog(this.getClass))


}
