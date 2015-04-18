package org.uqbar.wollok.choco.actor.traits

import org.uqbar.chocolate.core.reactions.events.MouseMoved
import org.uqbar.chocolate.core.reactions.events.Pressed
import org.uqbar.chocollok.actor.Actor

import org.uqbar.chocolate.core.reactions.io.MouseButton.Left

import org.uqbar.math.vectors.Vector

/**
 * Track the mouse movement and moves the actor to the same
 * place.
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
    case MouseMoved(position) => _mousePosition = position
    case Pressed(Left) =>  move(_mousePosition - this.translation)
  }
}