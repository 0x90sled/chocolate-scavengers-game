package org.uqbar.wollok.choco.actor.traits

/**
 * 
 */
object Easing {
  /**
   * t: currenttime
   * b: start value
   * c: change in value
   * d: duration
   */
  type EasingFunction = (Double, Double, Double, Double)=>Double

  /** just to simplify function definitions, without writting types */
  def easing(func : EasingFunction) = func
  
  // ********************
	// ** functions
  // ********************
  
  val linear = easeIn(1) _
  
  // quadratic
  val easeInQuad = easeIn(2) _
  
  // REVIEW: i'm not sure why this is not just easeOut(2) _ Formulas are different
  val easeOutQuad = easing{ (t0,b,c,d) => 
     val t = t0 / d
     -c * t * (t - 2) + b
  }
  val easeInOutQuad = easing { (t0,b,c,d) => 
     var t = t0 / (d/2)
     if (t < 1)
        c / 2 * Math.pow(t,2) + b
     else {
       t -=1
       -c/2 * (t * (t - 2) - 1) + b
     }
  }
  
  // cubic
  val easeInCubic = easeIn(3) _
  val easeOutCubic = easeOut(3) _
  val easeInOutCubic = easeInOut(3) _
  
  // quartic
  val easeInQuart = easeIn(4) _
  val easeOutQuart = easeOut(4) _
  val easeInOutQuart = easeInOut(4) _
  
  // quintic
  val easeInQuint = easeIn(5) _
  val easeOutQuint = easeOut(5) _
  val easeInOutQuint = easeInOut(5) _
  
  // sinusoidal
  val easeInSine = easing{ (t,b,c,d) => 
    -c * Math.cos(t / d * (Math.PI / 2)) + c + b
  }
  val easeOutSine = easing{ (t,b,c,d) =>
    c * Math.sin(t/d * (Math.PI/2)) + b
  }
  
  
  // REUSING Definitions
  private def easeIn(pow:Double) (t:Double, b:Double, c:Double, d:Double) = c * Math.pow(t / d, pow) + b
  private def easeOut(pow:Double) (t0:Double, b:Double, c:Double, d:Double) = {
    var t = (t0 / d) - 1
    c * (Math.pow(t, pow) + 1) + b
  }
  private def easeInOut(pow:Double) (t0:Double, b:Double, c:Double, d:Double) = {
    var t = t0 / (d/2)
    if (t < 1) 
      c/2 * (Math.pow(t, pow)) + b
    else {
      val symbol = if (pow % 2 == 0) -1 else 1
      t -= 2
      symbol * c/2 * (Math.pow(t, pow) + (symbol * 2)) + b
    }
  }

}