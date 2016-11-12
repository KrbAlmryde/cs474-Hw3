package com.hw3.Actors

import java.io.File

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

    var supervisor = self

    def receive: Receive = {

        // This is the entry point.
        case WakeUp => {
//            println(s"${self.path.name}: I awakened from ${sender.path.name}")
            // ProcessActor ! CloneRepo
            supervisor = sender
            context.actorOf(Props[ProcessActor], name = "cloneProcess") ! CloneRepo(repo.id.toString(), repo.name.get, repo.html_url.get)
        }

        // Clone has completed, now we can do the real work!
        // Create the understand Actor and generate the dependency graph
        case CloneResult(0) => {
            sender ! PoisonPill
            val undActor = context.actorOf(Props[UnderstandActor], name = "understand")
            val patchActor = context.actorOf(Props[PatchActor], name = "patches")

            /*
                    !! GENERATE THE PATCH !!
                    !! GENERATE .UDB FILE !!
             */
            patchActor ! GenPatch(repo.id.toString, repo.name.get)
//            undActor ! GenUDB(repo.id.toString, repo.name.get, repo.language.get)
        }

        // The Dependency Graph file has been successfully parsed!
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
        case FinalOutput(results) => {
            supervisor ! FinalOutput(results)
        }
//        case PatchResult(0, id) => {
//            println (s"${self.path.name}: Patch mad it!!")
//
//            val patchFile = s"KrbAlmryde_Data/resources/${repo.id}/1"
//
//            if ( new File(patchFile).exists ) {
//
//            } else
//                println("File not Found! Bummer...")
//        }


        case CloneResult(_) => {
            println ("THere was a problem cloning the repo! Shutting down")
            self ! PoisonPill // kill your self!
        }

        case _ => println(s"\n${self.path.name}: That cant be good...\n")
    }
}
