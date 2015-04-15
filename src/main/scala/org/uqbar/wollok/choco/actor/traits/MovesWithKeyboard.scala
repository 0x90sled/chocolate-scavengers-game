package org.uqbar.wollok.choco.actor.traits

import org.uqbar.chocolate.core.components.Visible
import org.uqbar.chocolate.core.dimensions.Positioned
import org.uqbar.chocolate.core.reactions.events.Hold
import org.uqbar.chocolate.core.reactions.io.Key.Letter.A
import org.uqbar.chocolate.core.reactions.io.Key.Letter.D
import org.uqbar.chocolate.core.reactions.io.Key.Letter.S
import org.uqbar.chocolate.core.reactions.io.Key.Letter.W
import org.uqbar.chocolate.core.reactions.io.Key.Navigation.Arrow.Down
import org.uqbar.chocolate.core.reactions.io.Key.Navigation.Arrow.Left
import org.uqbar.chocolate.core.reactions.io.Key.Navigation.Arrow.Right
import org.uqbar.chocolate.core.reactions.io.Key.Navigation.Arrow.Up
import org.uqbar.math.vectors.Touple_to_Vector
import org.uqbar.chocolate.core.reactions.events.Pressed

trait Actor extends Visible with Positioned {
}

/**
 * @author jfernandes
 */
trait MovesWithKeyboard extends Actor {
  in {
     case Pressed(W) ⇒ moveUp()
     case Pressed(Up(_)) ⇒ moveUp()
     
     case Pressed(S) ⇒ moveDown()
     case Pressed(Down(_)) ⇒ moveDown()
     
     case Pressed(A) ⇒ moveLeft()
     case Pressed(Left(_)) ⇒ moveLeft()
     
     case Pressed(Right(_)) ⇒ moveRigth()
     case Pressed(D) ⇒ moveRigth()
  }
  def moveUp() { move(0, -velocity) }
  def moveDown() { move(0, velocity) }
  def moveLeft() { move(-velocity, 0) }
  def moveRigth() { move(velocity, 0) }
  
  def velocity : Double
}