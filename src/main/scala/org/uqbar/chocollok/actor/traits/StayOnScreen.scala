package org.uqbar.chocollok.actor.traits

import org.uqbar.chocollok.actor.Actor
import org.uqbar.math.vectors.Vector
import org.uqbar.cacao.Rectangle

/**
 * Keeps the actor within the screen.
 * It won't perform the movement if it lead him outside the screen.
 * "Outside" means any part of its appearance won't be visible.
 */
trait StayOnScreen extends Actor {
  //allow to specify a "border" kind of allowing some percentage of the actor to go out 

  abstract override def move(delta:Vector) {
    // TODO other bounds
    val newPosition = translation + delta
    val body = new Rectangle(newPosition, size)
    if (isWithinScreen(body)) {
      super.move(delta)
    }
  }

  def isWithinScreen(p: Rectangle) = 
    p.from.x > 0 && p.from.y > 0 && (p.from.x + p.size.x) < scene.game.displaySize.x && (p.from.y + p.size.y) < scene.game.displaySize.y    
  
}