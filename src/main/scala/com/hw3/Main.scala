package com.hw3

// My stuff
import java.io.File

import com.hw3.Utils._
import com.hw3.Actors._
import com.hw3.Patterns.Messages.Language

import scala.io.StdIn

// Akka stuff
import akka.actor.{ActorSystem, Props}

//  http stuff
import akka.http.scaladsl.Http

// stream stuff
import akka.stream.ActorMaterializer


/**
  * Created by krbalmryde on 10/27/16.
  */
object Main extends App {

    implicit val system = ActorSystem("HelloSystem")
    implicit val materializer = ActorMaterializer()
    import system.dispatcher

    val masterActor = system.actorOf(Props[MasterActor], name = "master")

    val greeting:String = "Hello! From the following list:\n".concat(languages.foldRight("\n")(_+", "+_))

    var lang = "java"
    /*
    while(!isLegalLang(lang)) {
        try {
            lang = StdIn.readLine("Please enter Language: > ")
            if (!isLegalLang(lang)) println(s"Im sorry, but $lang is not a valid entry! Try again...")
        } catch {
            case e: NullPointerException => {
                println("Thanks for playing!")
            }
        }
    }
    */

    masterActor ! Language(lang)

    //     Sleep for a moment
    Thread.sleep(200000)
    println("Thanks for playing!")
    // Shut the system down
    Http().shutdownAllConnectionPools().onComplete(_ => system.terminate())


}


