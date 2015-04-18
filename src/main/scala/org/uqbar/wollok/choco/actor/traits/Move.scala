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
import org.uqbar.chocolate.core.reactions.io.MouseButton.{Left => MouseLeft}
import org.uqbar.math.vectors.MutableVector
import org.uqbar.math.vectors.Touple_to_Vector
import org.uqbar.math.vectors.Vector
import org.uqbar.cacao.Renderer
import org.uqbar.chocolate.core.appearances.Appearance
import org.uqbar.chocolate.core.reactions.events.Update

trait BaseKeyboardMovement extends Actor {
  in {
     case Pressed(W) ⇒ onKeyUp() ; case Pressed(Up(_)) ⇒ onKeyUp()
     case Pressed(S) ⇒ onKeyDown() ; case Pressed(Down(_)) ⇒ onKeyDown()
     case Pressed(A) ⇒ onKeyLeft() ; case Pressed(Left(_)) ⇒ onKeyLeft()
     case Pressed(Right(_)) ⇒ onKeyRight() ; case Pressed(D) ⇒ onKeyRight()
  }
  def onKeyUp()
  def onKeyDown()
  def onKeyLeft()
  def onKeyRight()
  
  def velocity : Double  // show be a vector (?)
}

/**
 * Tracks directional keyboards and moves the player: up, down, left, right.
 * 
 * @author jfernandes
 */
trait MovesWithKeyboard extends BaseKeyboardMovement {
  override def onKeyUp() { move(0, -velocity) }
  override def onKeyDown() { move(0, velocity) }
  override def onKeyLeft() { move(-velocity, 0) }
  override def onKeyRight() { move(velocity, 0) }
}

/**
 * Tracks directions on keyboard to move the actor.
 * But the movement rotational. Left and Right arrows will rotate the actor, while up and down
 * will make it go forewards or backwards.
 */
trait RotationalMovement extends BaseKeyboardMovement {
  class Direction(var dir: Double) { }
  object Foreward extends Direction(-1d)
  object Backward extends Direction(1d)
  
  var angle = 0d
  val baseAngle = Math.PI / 2
  var rotationVelocity = 10

  def absoluteAngle() = baseAngle + angle
  
  override def onKeyLeft() { rotate(-rotationVelocity) }
  override def onKeyRight() { rotate(rotationVelocity) }
  
  def rotate(delta: Double) { angle += Math.toRadians(delta) }

  override def onKeyUp() { move(Foreward) }
  override def onKeyDown() { move(Backward) }
  
  def move(direction : Direction) {
    move(direction.dir * velocity * Math.cos(absoluteAngle), direction.dir * velocity * Math.sin(absoluteAngle))
  }
  
  abstract override def render(renderer: Renderer) = {
    // HACK !
    this.synchronized {
      val backup = appearance.clone
      
      appearance match {
        case a: Animation => a.rotate(angle)
      }
      super.render(renderer)
      appearance = backup
    }
  }
}