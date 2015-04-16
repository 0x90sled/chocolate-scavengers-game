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

/**
 * An actor has a current State
 */
trait Actor extends Visible with Positioned {
  var state : State = _ // REVIEWME: should start with a default state
  // and a predefined image (?)
  
  override def appearance = state.appearance
  def appearance_=(app:Appearance) { state.appearance = app }
  
  in { 
    case Update(delta) ⇒ state.update(delta)
    //case e => state.handle(e)
  }
}

trait BaseMovement extends Actor {
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
trait MovesWithKeyboard extends BaseMovement {
  override def onKeyUp() { move(0, -velocity) }
  override def onKeyDown() { move(0, velocity) }
  override def onKeyLeft() { move(-velocity, 0) }
  override def onKeyRight() { move(velocity, 0) }
}

/**
 * Track the mouse movement and moves the actor to the same
 * place.
 * 
 * @author jfernandes
 */
trait FollowMouse extends Actor {
  in { case MouseMoved(position) => move(position - this.translation) }
}

/**
 * Like graphical adventures, the actor will move to the mouse current's position
 * on a click.
 */
trait FollowMouseClick extends Actor {
  protected var _mousePosition: Vector = (0, 0)
  in { 
    //TODO: interpolate
    case MouseMoved(position) => _mousePosition = position
    case Pressed(MouseLeft) =>  move(_mousePosition - this.translation)
  }
}

/**
 * Tracks directions on keyboard to move the actor.
 * But the movement rotational. Left and Right arrows will rotate the actor, while up and down
 * will make it go forewards or backwards.
 */
trait RotationalMovement extends BaseMovement {
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


//trait FlipHorizontally extends BaseMovement {
//  abstract override def moveLeft() {
//    // flip
//    // super 
//  }
//}