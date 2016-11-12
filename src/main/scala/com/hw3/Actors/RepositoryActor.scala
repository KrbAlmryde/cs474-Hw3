package com.hw3.Actors

import akka.actor.{Actor, PoisonPill, Props}
import com.hw3.Patterns.JsonProtocol.Repo
import com.hw3.Patterns.Messages._
import org.json4s.JsonAST.JValue

/**
  * Created by krbalmryde on 11/2/16.
  */

object RepositoryActor {
    /**
      * Creates Props for an actor of this type
      *
      * Allows to instantiate actor with Json results of relevant repository
      * which will be used frequently by Actor system
      */

    def props(repo:Repo): Props = {
//        println(s"Instantiating RepoActor: $repo")
        Props(new RepositoryActor(repo))
    }
}

class RepositoryActor(repo:Repo) extends Actor{


    def receive: Receive = {

        // This is the entry point.
        case WakeUp => {
            println(s"${self.path.name}: I awakened from ${sender.path.name}")
            // ProcessActor ! CloneRepo
            context.actorOf(Props[ProcessActor], name = "cloneProcess") ! CloneRepo(repo.id.toString(), repo.name.get, repo.html_url.get)
        }

        // Clone has completed, now we can do the real work!
        // Create the understand Actor and generate the dependency graph
        case CloneResult(0) => {
            sender ! PoisonPill
            val undActor = context.actorOf(Props[UnderstandActor], name = "understand")
            val patchActor = context.actorOf(Props[ProcessActor], name = "patches")

            // This one seems faster, do it first
            patchActor ! GenPatch(repo.id.toString, repo.name.get)
//            undActor ! GenUDB(repo.id.toString, repo.name.get, repo.language.get)
        }

        // The Dependency Graph file has been
        case DepGraphResult(graph) => {
            println(s"Received a Dependency Graph from ${sender.path.name}!")
            var counter = 50
            val edges = graph.edgeSet.toArray

            if (edges.size < 50)
                counter = edges.size

            for ( i <- 0 until counter) {
                println("\t"+edges(i))
            }

            context.actorOf(Props[ProcessActor], name="Reset") ! CleanRepo(repo.id.toString)
        }

        case PatchResult(0) => {
            println (s"${self.path.name}: Got the output here!")
        }

        case PatchResult(_) => {
            println (s"${self.path.name}: THere was a problem making a patch!!")
        }

        case CloneResult(_) => {
            println ("THere was a problem cloning the repo! Shutting down")
            self ! PoisonPill // kill your self!
        }

        case _ => println(s"\n${self.path.name}: That cant be good...\n")
    }
}
