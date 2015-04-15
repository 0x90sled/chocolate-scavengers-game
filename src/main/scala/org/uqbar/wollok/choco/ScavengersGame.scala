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

object ScavengersGame extends Game {
  def title = "Scavengers Game !"
  def displaySize = (800, 600)
  
  currentScene.addComponent(new Scavenger)
}

class Scavenger extends Actor with MovesWithKeyboard {
    translation = (10, 10)
    val splittedSprites = ResourceLoader.loadSprite("/scavengers/Scavengers_SpriteSheet.png").split(32, 32)
    splittedSprites.foreach { _.scale(3, 3) }
    
    val defaultStateSprites = splittedSprites.slice(0, 5)
    val attackingStateSprites = splittedSprites.slice(40, 41)
    val hitStateSprites = splittedSprites.slice(46, 47)
    
    val defaultState = new DefaultState(this, new Animation(0.2, defaultStateSprites)) 
    val attackingState = new TimeLimitedState(this, new Animation(0.4, attackingStateSprites), 0.8, defaultState)
    val hitState = new TimeLimitedState(this, new Animation(0.4, hitStateSprites), 0.8, defaultState)
    
    state = defaultState
    
    def velocity = 7
    in {
     case Pressed(Space) ⇒ state = attackingState
     case Pressed(H) ⇒ state = hitState
     case Update(delta) ⇒ state.update(delta)
    }
}
