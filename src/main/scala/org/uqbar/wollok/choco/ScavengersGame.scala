package org.uqbar.wollok.choco

import org.uqbar.chocolate.core.Game
import org.uqbar.chocolate.core.appearances.Animation
import org.uqbar.chocolate.core.appearances.Appearance
import org.uqbar.chocolate.core.loaders.ResourceLoader
import org.uqbar.chocolate.core.reactions.events.Pressed
import org.uqbar.chocolate.core.reactions.events.Update
import org.uqbar.chocolate.core.reactions.io.Key.Special.Space
import org.uqbar.math.vectors.Touple_to_Vector
import org.uqbar.wollok.choco.actor.traits.{Actor, MovesWithKeyboard}
import org.uqbar.wollok.choco.actor.traits.FollowMouse
import org.uqbar.wollok.choco.actor.traits.RotationalMovement
import org.uqbar.chocolate.core.reactions.io.Key.Letter.H
import org.uqbar.wollok.choco.actor.traits.TimeLimitedState
import org.uqbar.wollok.choco.actor.traits.DefaultState
import org.uqbar.chocolate.core.components.Collisionable
import org.uqbar.chocolate.core.collisions.BoundingBox
import org.uqbar.chocolate.core.collisions.CircularBoundingBox
import org.uqbar.chocolate.core.reactions.events.Collision

object ScavengersGame extends Game {
  def title = "Scavengers Game !"
  def displaySize = (800, 600)
  val splittedSprites = ResourceLoader.loadSprite("/scavengers/Scavengers_SpriteSheet.png").split(32, 32)
  
  currentScene.addComponent(new Scavenger)
  currentScene.addComponent(new Zombie1)
  

  /**
   * Main character
   */
  class Scavenger extends Actor with MovesWithKeyboard with Collisionable {
    translation = (10, 10)
    splittedSprites.foreach { _.scale(3, 3) }
    
    val defaultStateSprites = splittedSprites.slice(0, 6)
    val attackingStateSprites = splittedSprites.slice(40, 42)
    val hitStateSprites = splittedSprites.slice(46, 48)
    
    val defaultState = new DefaultState(this, new Animation(0.2, defaultStateSprites)) 
    val attackingState = new TimeLimitedState(this, new Animation(0.3, attackingStateSprites), 0.6, defaultState)
    val hitState = new TimeLimitedState(this, new Animation(0.3, hitStateSprites), 0.6, defaultState)
    
    state = defaultState
    
    def velocity = 7
    in {
     case Pressed(Space) ⇒ state = attackingState
     case Pressed(H) ⇒ state = hitState
     case Collision(z) if (z.isInstanceOf[Enemy]) ⇒ state = hitState
    }

    def boundingBox = new CircularBoundingBox(29)
  }
  
  trait Enemy {}
  
  class Zombie1 extends Actor with Collisionable with Enemy {
    translation = (100, 100)
    val defaultState = new DefaultState(this, new Animation(0.2, splittedSprites.slice(6,12))) 
    val attackingState = new TimeLimitedState(this, new Animation(0.3, splittedSprites.slice(42,44)), 0.6, defaultState)
    state = defaultState
    
    in {
     case Collision(c) if (c.isInstanceOf[Scavenger]) ⇒ state = attackingState
    }
    
    def boundingBox = new CircularBoundingBox(29)
  }
  
}