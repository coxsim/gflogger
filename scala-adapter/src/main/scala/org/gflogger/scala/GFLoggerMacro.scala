package org.gflogger.scala

import org.gflogger.{GFLogEntry, GFLog => Underlying}

import scala.reflect.api.Trees
import scala.reflect.macros.blackbox.Context


/**
  * Created by simon on 1/10/2016.
  */
private object GFLogMacro {

  type LoggerContext = Context { type PrefixType = GFLog }

  // Error

  def errorMessage(c: LoggerContext)(message: c.Expr[String]) = {
    import c.universe._
    val underlying = q"${c.prefix}.underlying"
    q"if ($underlying.isErrorEnabled) $underlying.error($message)"
  }

  def errorFunction(c: LoggerContext)(function: c.Expr[GFLogEntry => Unit]): c.Expr[Unit] = {
    import c.universe._

    val underlying = q"${c.prefix}.underlying"

    val q"(..$args) => $body" = function.tree
    val q"$x; ()" = body

    // TODO: should be a way to get tail recursion working, but fails with path dependant type issues
    def loop(inputPrefix: Trees#Tree): Tree = {
      inputPrefix match {
        case q"$newInputPrefix.append($message)" =>
          val outputPrefix = loop(newInputPrefix)
          q"$outputPrefix.append($message)"
        case _ => q"if ($underlying.isErrorEnabled()) $underlying.error()"
      }
    }
    val prefix = loop(x)
    val tree = q"$prefix.commit()"

//
//    @tailrec def loop(inputPrefix: Trees#Tree, messages: List[Trees#Tree]): List[Trees#Tree] = {
//      inputPrefix match {
//        case q"$newInputPrefix.append($message)" => loop(newInputPrefix, message +: messages)
//        //case _ => q"$outputPrefix.commit()"
//        case _ => messages
//      }
//    }
//    val messages = loop(x, List.empty[Trees#Tree])
//
//    @tailrec def build(prefix: Tree, messages: List[Trees#Tree]): Tree = {
//      if (messages.isEmpty) q"$prefix.commit()"
//      else build(q"$prefix.append(${messages.head})", messages.tail)
//    }
//    val tree = build(q"if ($underlying.isErrorEnabled()) $underlying.error()", messages)

    c.Expr(tree)
  }

  // Debug

  def debugMessage(c: LoggerContext)(message: c.Expr[String]) = {
    import c.universe._
    val underlying = q"${c.prefix}.underlying"
    q"if ($underlying.isDebugEnabled) $underlying.debug($message)"
  }
}
