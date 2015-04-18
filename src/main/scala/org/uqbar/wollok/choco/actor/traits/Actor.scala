package org.uqbar.wollok.choco.actor.traits

import org.uqbar.chocolate.core.appearances.Animation
import org.uqbar.chocolate.core.components.Visible
import org.uqbar.chocolate.core.dimensions.Positioned
import org.uqbar.chocolate.core.reactions.events.MouseMoved
import org.uqbar.chocolate.core.reactions.events.Pressed
import org.uqbar.chocolate.core.reactions.io.Key.Letter.A
import org.uqbar.chocolate.core.reactions.io.Key.Letter.D
import org.uqbar.chocolate.core.reactions.io.Key.Letter.S
import org.uqbar.chocolate.core.reactions.io.Key.Letter.W
import org.uqbar.chocolate.core.reactions.io.Key.Navigation.Arrow.Down
import org.uqbar.chocolate.core.reactions.io.Key.Navigation.Arrow.Left
import org.uqbar.chocolate.core.reactions.io.Key.Navigation.Arrow.Right
import org.uqbar.chocolate.core.reactions.io.Key.Navigation.Arrow.Up
import org.uqbar.chocolate.core.reactions.io.MouseButton.{ Left => MouseLeft }
import org.uqbar.math.vectors.MutableVector
import org.uqbar.math.vectors.Touple_to_Vector
import org.uqbar.math.vectors.Vector
import org.uqbar.cacao.Renderer
import org.uqbar.chocolate.core.appearances.Appearance
import org.uqbar.chocolate.core.reactions.events.Update
import scala.collection.mutable.ListBuffer

/**
 * An actor has a current State
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
      synchronized {actions.clone().foreach { a =>
        if (!a.update(d)) {
          println("Removing action")
          actions.remove(actions.indexOf(a))
        }
      }
    }
  }
  def addAction(a: Action) { actions += a }
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