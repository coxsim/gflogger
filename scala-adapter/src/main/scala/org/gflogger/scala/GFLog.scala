package org.gflogger.scala

import scala.language.experimental.macros
import org.gflogger.{GFLogEntry, GFLog => Underlying}

object GFLog {
  def apply(underlying: Underlying): GFLog = new GFLog(underlying)
}

/**
  * Created by simon on 1/10/2016.
  */
class GFLog(val underlying: Underlying) {
  def error(message: String): Unit = macro GFLogMacro.errorMessage
  def error(function: GFLogEntry => Unit): Unit = macro GFLogMacro.errorFunction

  def debug(message: String): Unit = macro GFLogMacro.debugMessage
}
