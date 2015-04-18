package org.uqbar.wollok.choco

import org.scalatest.FunSuite
import org.uqbar.wollok.choco.actor.traits.InterpolatingValueGenerator
import org.uqbar.wollok.choco.actor.traits.Interpolator

class InterpolationTest extends FunSuite {

  test("Interpolate Negative values") {
    val generator = new InterpolatingValueGenerator(10, -10, 1000, Interpolator.linear)

    assert(8.0 == generator.generate(100))
    assert(6.0 == generator.generate(100)) 
    assert(4.0 == generator.generate(100)) 
    assert(0 == generator.generate(200)) // 500ms passed
    assert(-4.0 == generator.generate(200)) 
    assert(-10.0 == generator.generate(300)) // 1000 end
  }
  
}