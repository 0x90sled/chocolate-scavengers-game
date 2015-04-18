package org.uqbar.wollok.choco.actor.traits

import org.uqbar.cacao.Rectangle
import org.uqbar.cacao.Renderer
import org.uqbar.chocolate.core.components.GameComponent
import org.uqbar.chocolate.core.dimensions.Bounded
import org.uqbar.chocolate.core.dimensions.Positioned
import org.uqbar.chocolate.core.loaders.ResourceLoader
import org.uqbar.chocolate.core.reactions.events.Render
import org.uqbar.math.vectors.Touple_to_Vector
import org.uqbar.cacao.Color

/**
 * @author jfernandes
 */
class DebugActorInfo extends GameComponent {
  
  in {
    case Render(r) => render(r)
  }
  def render(renderer:Renderer) {
    scene.components.foreach { a => renderBox(a, renderer) }
  }

  def renderBox(a: GameComponent, renderer: Renderer) = {
    a match {
      case v: Bounded with Positioned => renderDebugInfo(v, renderer)
      case _ =>
    }
  }

  def renderDebugInfo(v: Bounded with Positioned, renderer: Renderer)  = {
    renderer draw Rectangle((v.left, v.top), v.size)
    renderer.font = ResourceLoader.font('monospaced, 10)
    renderer.color = Color.Green
    renderer.draw(s"(${v.left.round}, ${v.top.round})") (v.left, v.top - 10)
  }
}