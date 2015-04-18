package org.uqbar.wollok.choco

import org.uqbar.chocolate.core.components.GameComponent
import org.uqbar.chocolate.core.reactions.events.Render
import org.uqbar.cacao.Line
import org.uqbar.cacao.Rectangle
import org.uqbar.cacao.Color
import org.uqbar.math.vectors.Vector

class Grid(var cellSize : Vector) extends GameComponent {
  val gridColor = Color(0, 0, 0, 127)
  
  in {
    case Render(renderer) =>
      val gameWidth = game.displaySize.x.toInt
      val gameHeight = game.displaySize.y.toInt

      renderer.color = gridColor
      for (i ← 0.to(gameWidth, cellSize.x.toInt)) renderer draw Line((i, 0), (i, gameHeight))
      for (j ← 0.to(gameHeight, cellSize.y.toInt)) renderer draw Line((0, j), (gameWidth, j))

      renderer.color = gridColor
      renderer draw Rectangle((0, 0), (gameWidth - 1, gameHeight - 1))
  }

}