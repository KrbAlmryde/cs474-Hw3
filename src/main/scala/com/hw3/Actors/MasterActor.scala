package com.hw3.Actors

import akka.actor.{Actor, PoisonPill, Props}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.hw3.Patterns.JsonProtocol.SearchResult
import com.hw3.Patterns.Messages._
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JValue



/**
  * Created by krbalmryde on 10/30/16.
  */

class MasterActor extends Actor {

    // Implicits
    implicit val formats = DefaultFormats // Brings in default date formats etc.
    implicit val languages = List("Ada", "Assembly", "C", "C++", "C#", "FORTRAN", "Java", "JOVIAL", "Delphi", "Pascal", "PL", "M", "VHDL", "Cobol", "PHP", "HTML", "CSS", "JavaScript", "Python").map( _.toLowerCase )

//    final implicit val materializer:ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

    def receive = {

        case Language(x) if languages.contains( x.toLowerCase ) => {
            println(s"Dispatching GitHub API to search for $x")
            context.actorOf(Props[GitApiActor], name = "gitSearch") ! Search(x.toLowerCase)
        }

        // The resulting JSON object from the SearchRequest
        case JsonResult(json) => {
            sender() ! PoisonPill
            println (s"${sender.path.name} sent us some $json")
            json.extract[SearchResult].items.foreach(repo => {
                val repoActor = context.actorOf(Props[RepoClientActor], name=s"repo-${repo.id}")
                repoActor ! repo
            })
        }


        // To be used when a RepoClient sends the completed response back
        case FinalOutput(x) => {
            sender() ! PoisonPill
        }


        /* For Actor Debugging */
        case MyMessage(m) => {
            println(s"${self.path.name}:I received the message: $m")
            sender() ! Done(s"I got your message! -${self.path.name} ")
        }

        case Done(m) => {
            println(s"${self.path.name}: $m")
        }


        case x => {
            println(s"${self.path.name}: $x Was NOT What I was expecting...")
        }

    }
}
