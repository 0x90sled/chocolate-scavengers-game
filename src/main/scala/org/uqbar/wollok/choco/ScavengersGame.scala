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

object ScavengersGame extends Game {
  def title = "Scavengers Game !"
  def displaySize = (800, 600)
  
  currentScene.addComponent(new Scavenger)
}

class Scavenger extends Actor with MovesWithKeyboard  {
    translation = (10, 10)
    val allSprites = ResourceLoader.loadSprite("/scavengers/Scavengers_SpriteSheet.png") 
    
    val defaultStateSprites = (0 to 5) map { i => 
      val s = allSprites.clone
      s.crop(i * 32,0)(32, 32)
      s.scale(2, 2) 
      s
    }
    
    val attackingStateSprites = (0 to 1) map { i => 
      val s = allSprites.clone
      s.crop(i * 32,32 * 5)(32, 32)
      s.scale(2, 2) 
      s
    }
    
    val defaultState = new DefaultState(this, new Animation(0.2, defaultStateSprites:_*)) 
    val attackingState = new TimeLimitedState(this, new Animation(0.4, attackingStateSprites:_*), 0.8, defaultState)
    
    var state : State = defaultState
    override def appearance = state.appearance
    
    def velocity = 7
    in {
     case Pressed(Space) ⇒ state = attackingState
     case Update(delta) ⇒ state.update(delta)
    }
}
abstract class State(var actor : Scavenger, var appearance : Appearance) {
  def update(delta: Double) : Unit
}
class DefaultState(a : Scavenger, app : Appearance) extends State(a, app) {
  override def update(delta: Double) = { }
}
class TimeLimitedState(a : Scavenger, app : Appearance, var duration : Double, var nextState : State) extends State(a, app) {
  private var timeToEnd = duration
  override def update(delta: Double) = { 
    timeToEnd -= delta
    if (timeToEnd < 0) {
      actor.state = nextState
      timeToEnd = duration
    }
  }
}
