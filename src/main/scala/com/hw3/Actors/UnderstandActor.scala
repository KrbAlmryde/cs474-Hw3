package com.hw3.Actors

import akka.actor.{Actor, PoisonPill, Props}
import com.hw3.Patterns.Messages._

/**
  * Created by krbalmryde on 10/29/16.
  */
class UnderstandActor extends Actor {
    var supervisor = self
    var repo = RepoDetails("","","","")

    def receive = {

        case RepoDetails(id, name, url, language) => {
            supervisor = sender
            repo = RepoDetails(id, name, url, language)
            context.actorOf(Props[ProcessActor], name = "cloneProcess") ! CloneRepo(id, name, url)
        }

        /* ------------------------ *
          * Process Result Messages *
          * ----------------------- */
        case CloneResult(0) => {
            sender ! PoisonPill
            context.actorOf(Props[ProcessActor], name="udbProcess") ! UDB(repo.id, repo.name, repo.lang)
            context.actorOf(Props[ProcessActor], name="cleanProcess") ! CleanRepo(repo.id, repo.name)
        }

        // Result of Generating the .udb file
        case UDBResult(0) => {
            sender ! PoisonPill
//            context.actorOf(Props[DepGraph], name="depGraph") ! DepGraph(repo.id, repo.name)
        }


        /* --------------------- *
         * In case Process fails *
         * --------------------- */
        case CloneResult(1) => {
            println("The repo failed to clone")
            //context.actorOf(Props[ProcessActor], name="cleanProcess") ! CleanRepo(repo.id, repo.name)
            context.stop(self)
        }

        case UDBResult(1) => {
            sender ! PoisonPill
            context.stop(self)
        }
    }


}
