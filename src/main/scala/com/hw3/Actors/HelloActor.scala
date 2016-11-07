package com.hw3.Actors

import akka.actor.Actor
import com.hw3.Patterns.Messages.{Done, Hello, MyMessage}

/**
  * Created by krbalmryde on 10/30/16.
  */

object HelloActor {
    case class Greeting(from:String)
    case class Received(from:String)
}
class HelloActor extends Actor {

    def receive = {
        case Hello  => println("hello, world")

        case MyMessage(m) => {
            println(s"${self.path.name}:I received the message: $m")
            sender() ! Done(s"I got your message! -${self.path.name} ")
        }

        case Done(m) => println(s"${self.path.name}: $m")

        case _ => println("huh?")
    }
}

