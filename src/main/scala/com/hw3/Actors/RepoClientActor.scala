package com.hw3.Actors

import akka.actor.{Actor, Props}
import com.hw3.Patterns.JsonProtocol.Repo
import com.hw3.Patterns.Messages.{JsonResult, RepoDetails}

/**
  * Created by krbalmryde on 11/2/16.
  */



class RepoClientActor extends Actor{

    def receive: Receive = {
        case repo:Repo => {
            println(s"I received $repo from ${sender.path.name}")

            val undActor = context.actorOf(Props[UnderstandActor], name = "understand")
            val name = repo.name.get
            val url = repo.html_url.get
            val id = repo.id.toString()
            val lang = repo.language.get

            undActor ! RepoDetails(id, name, url, lang)
        }

        case JsonResult(content) => {

        }
    }
}
