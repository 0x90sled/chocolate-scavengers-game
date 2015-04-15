
package org.uqbar.wollok.choco.actor.traits

import org.uqbar.chocolate.core.appearances.Appearance

/**
 * Base class for all states.
 * A state has an appearance (so that it could change in different states)
 * An receives the "update()" event so it can do anything like computing
 * time and transition to another state.
 */
abstract class State(var actor : Actor, var appearance : Appearance) {
  def update(delta: Double) : Unit
}

/**
 * Default state. Stays here forever.
 */
class DefaultState(a : Actor, app : Appearance) extends State(a, app) {
  override def update(delta: Double) = { }
}

/**
 * A time-boxed state.
 * It will stay in this state just for a limited amount of time
 * Then it will get back to a given state (nextState)
 */
class TimeLimitedState(a : Actor, app : Appearance, var duration : Double, var nextState : State) extends State(a, app) {
  private var timeToEnd = duration
  override def update(delta: Double) = { 
    timeToEnd -= delta
    if (timeToEnd < 0) {
      actor.state = nextState
      timeToEnd = duration
    }
  }
}