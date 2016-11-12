package com.hw3.Actors

import akka.actor.{Actor, PoisonPill, Props}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.hw3.Utils.App_Start_Range
import com.hw3.Patterns.JsonProtocol.{Repo, SearchResult}
import com.hw3.Patterns.Messages._
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JValue



/**
  * Created by krbalmryde on 10/30/16.
  */

class MasterActor extends Actor {

    // Implicits
    implicit val formats = DefaultFormats // Brings in default date formats etc.

//    final implicit val materializer:ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

    def receive = {

        case Language(x) => {
            println(s"Dispatching GitHub API to search for $x")
            context.actorOf(Props[GitSearchActor], name = "gitSearch") ! GitSearch(x.toLowerCase)
        }

        // The resulting JSON object from the SearchRequest
        case JsonResult(json) => {
            sender() ! PoisonPill
            println (s"Master: ${sender.path.name} sent us some $json")

            json.extract[SearchResult].items.foreach(repo => {

                context.actorOf(RepositoryActor.props(repo), name=s"repo-${repo.name.get}") ! WakeUp
//                if ( App_Start_Range < repo.id  && repo.id < App_Start_Range+1000 ) {
//                    println (s"\t\t\tcreating ${repo.name}")
//                    // create Actor RepoClient(repo) named "repo-<repository id>
//                }
            })

//            json.extract[SearchResult].items.foreach(repo => {
//                val repoActor = context.actorOf(RepoClientActor.props(repo), name=s"repo-${repo.id}")
//                repoActor ! WakeUp
//            })
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
