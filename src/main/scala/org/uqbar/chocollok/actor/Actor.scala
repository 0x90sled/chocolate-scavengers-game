package org.uqbar.chocollok.actor

import org.uqbar.chocolate.core.components.Visible
import org.uqbar.chocolate.core.dimensions.Positioned
import org.uqbar.chocolate.core.appearances.Appearance
import org.uqbar.chocolate.core.reactions.events.Update
import scala.collection.mutable.ListBuffer

/**
 * An actor has a current State and actions
 */
trait Actor extends Visible with Positioned {
  var state: State = _ // REVIEWME: should start with a default state
  // and a predefined image (?)

  override def appearance = state.appearance
  def appearance_=(app: Appearance) { state.appearance = app }

  in { case Update(delta) ⇒ state.update(delta) }
}

/**
 * Adds capability to perfom actions.
 * An action can be recurrent or eventually finish.
 */
trait Actuator extends Actor {
  var actions: ListBuffer[Action] = ListBuffer()
  in {
    case Update(d) => 
      synchronized { actions.clone.foreach { a =>
        if (!a.update(d))
          removeAction(a)
      }
    }
  }
  def addAction(a: Action) { actions += a }
  def removeAction(a: Action) { actions -= a }
}

/**
 * Pretty generic, could be anything to execute periodically.
 */
trait Action {
  /**
   *  returns whether it should still be executed on further ticks
   *  if you return false the action will be removed
   */
  def update(delta: Double): Boolean
}