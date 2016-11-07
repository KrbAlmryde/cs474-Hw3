package com.hw3.Actors

import akka.actor.{Actor, Props}
import com.hw3.Patterns.JsonProtocol.{Repo, RepoDetails}
import com.hw3.Patterns.Messages.{Content, JsonResult, Search}

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

            undActor ! RepoDetails(name, url, id, lang)
        }

        case JsonResult(content) => {

        }
    }
}
