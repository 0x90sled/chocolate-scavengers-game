package org.uqbar.wollok.choco

import org.uqbar.chocolate.core.Game
import org.uqbar.chocolate.core.appearances.Animation
import org.uqbar.chocolate.core.appearances.Sprite
import org.uqbar.chocolate.core.collisions.CircularBoundingBox
import org.uqbar.chocolate.core.components.Collisionable
import org.uqbar.chocolate.core.components.Visible
import org.uqbar.chocolate.core.dimensions.Positioned
import org.uqbar.chocolate.core.loaders.ResourceLoader
import org.uqbar.chocolate.core.reactions.events.Collision
import org.uqbar.chocolate.core.reactions.events.CollisionEnd
import org.uqbar.chocolate.core.reactions.events.Pressed
import org.uqbar.chocolate.core.reactions.io.Key.Special.Space
import org.uqbar.math.vectors.Touple_to_Vector
import org.uqbar.math.vectors.Vector
import org.uqbar.wollok.choco.actor.traits.Actor
import org.uqbar.wollok.choco.actor.traits.DefaultState
import org.uqbar.wollok.choco.actor.traits.Interpolator
import org.uqbar.wollok.choco.actor.traits.MovesWithKeyboard
import org.uqbar.wollok.choco.actor.traits.SmoothMovable
import org.uqbar.wollok.choco.actor.traits.TimeLimitedState

object ScavengersGame extends Game {
  def title = "Scavengers Game !"
  def displaySize = (800, 600)
  val spriteCellSize : Vector = (32, 32)
  val spriteScalling : Vector = (3, 3)
  val spriteDisplaySize : Vector = (spriteCellSize.x * spriteScalling.x, spriteCellSize.y * spriteScalling.y) 
  val splittedSprites = ResourceLoader.loadSprite("/scavengers/Scavengers_SpriteSheet.png").split(spriteCellSize)
  splittedSprites.foreach { _.scale(spriteScalling) }
  
  val cellSize : Vector = (spriteDisplaySize.x * 1.1, spriteDisplaySize.y * 1.1)

  // background terrain
  currentScene.addComponent(new Visible with Positioned {
    var appearance = splittedSprites(38).repeat(10, 10)
  });
  
  // grid cells: a little bit (10%) bigger than characters
  currentScene.addComponent(new Grid(cellSize));
  
  // elements
  currentScene.addComponent(new Scavenger)
  currentScene.addComponent(new Zombie1)
  currentScene.addComponent(new Zombie2)

//  currentScene.addComponent(new DebugActorInfo)
  
  /**
   * Main character
   */
  class Scavenger extends Actor with MovesWithKeyboard with SmoothMovable with Collisionable with ScavengerCharacter {
    translation = cellSize * 0.1 // coupling with cellSize percentage
    
    val defaultStateSprites = splittedSprites.slice(0, 6)
    val attackingStateSprites = splittedSprites.slice(40, 42)
    val hitStateSprites = splittedSprites.slice(46, 48)
    
    val defaultState = new DefaultState(this, new Animation(0.2, defaultStateSprites)) 
    val attackingState = new TimeLimitedState(this, new Animation(0.3, attackingStateSprites), 0.6, defaultState)
    val hitState = new TimeLimitedState(this, new Animation(0.3, hitStateSprites), 0.6, defaultState)
    
    state = defaultState

    def velocity = cellSize.x.round
    override def easingFunction() = Interpolator.easeInSine
    in {
     case Pressed(Space) ⇒ state = attackingState
     case Collision(z : Enemy) ⇒ state = hitState
    }
  }
  
  trait ScavengerCharacter {
	  val collisionMargin : Vector = (0.8, 0.8) // percentage 
    // doesn't work well with a rectangular, a bug in chocolate (?)
    def boundingBox = new CircularBoundingBox(spriteDisplaySize.x * collisionMargin.x / 2)
  }
  
  // Enemies 
  
  trait Enemy extends Actor with Collisionable with ScavengerCharacter {
    val defaultState = new DefaultState(this, new Animation(0.2, defaultStateSprites())) 
    val attackingState = new DefaultState(this, new Animation(0.3, attackingStateSprites()))
    state = defaultState
    
    def defaultStateSprites() : Seq[Sprite]
    def attackingStateSprites() : Seq[Sprite]
    
    in {
     case Collision(c : Scavenger) ⇒ state = attackingState
     case CollisionEnd(c : Scavenger) ⇒ state = defaultState
    }
  }
  
  class Zombie1 extends Enemy {
    move(100, 100)
    def defaultStateSprites() = splittedSprites.slice(6,12)
    def attackingStateSprites() = splittedSprites.slice(42,44)
  }
  
  class Zombie2 extends Enemy {
    move(200, 100)
    def defaultStateSprites() = splittedSprites.slice(12,18)
    def attackingStateSprites() = splittedSprites.slice(44,46)
  }
  
}