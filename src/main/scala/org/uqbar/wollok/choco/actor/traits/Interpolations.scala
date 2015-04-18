package org.uqbar.wollok.choco.actor.traits

import org.uqbar.chocolate.core.dimensions.Positioned
import org.uqbar.math.vectors.Touple_to_Vector
import org.uqbar.math.vectors.Vector


/** 
 *  Kind of an interator of intermediate positions to move an object. 
 *  Calculates the next point to move to.
 *  Uses two ValueGenerators for 'x', and 'y' axis. 
 */
class Deplacement(var x : ValueGenerator, var y : ValueGenerator) {
  def nextPosition(delta: Double) = (x.generate(delta).round, y.generate(delta).round)
  def continue() = x.continue() || y.continue()
}


// *************************
// ** Generators
// *************************

/**
 * An object that generates values (one per call)
 * based on changes in time.
 * 
 * This objects can be used to interpolate any value like position, or rotation angles,
 * etc.
 */
trait ValueGenerator {
  def generate(deltaT: Double) : Double
  def continue() : Boolean = false
}

/** Returns the Positioned current value (component paremetrized: either x or y) */
class ConstantValueGenerator(val p: Positioned, val component: (Vector)=>Double) extends ValueGenerator {
  override def generate(deltaT:Double) = component(p.translation)
}

/**
 * Return a value based on an easingFunction.
 * Holds the state of the execution.
 */
class InterpolatingValueGenerator(val startValue : Double, val endValue : Double, val duration : Double, val easingFunction : Easing.EasingFunction)  extends ValueGenerator {
	var runtime = 0d
  var change = endValue - startValue
  var lastValue: Double = _
  
  override def generate(deltaT: Double) = {
    runtime += deltaT
    lastValue = easingFunction(runtime, startValue, change, duration)
    lastValue
  }

  def ended() = runtime >= duration || (lastValue.abs / endValue.abs > 0.99 && lastValue.abs / endValue.abs < 1.01)
  override def continue() = !ended()
}