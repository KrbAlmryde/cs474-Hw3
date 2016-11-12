package com.hw3.Actors

import akka.actor.{Actor, ActorPath, ActorRef, PoisonPill, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.hw3.Patterns.Messages._
import org.json4s._

/**
  * Created by krbalmryde on 11/1/16.
  */

class GitSearchActor() extends Actor {
    import akka.pattern.pipe
    import context.dispatcher

    // Implicits
    implicit val formats = DefaultFormats // Brings in default date formats etc.
    final implicit val materializer:ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

    // Useful abstractions
    val http = Http(context.system)
    val authorize = headers.Authorization(BasicHttpCredentials("KrbAlmryde", "VaZgxHQk2N"))

    var supervisor:ActorRef = self

    def receive: Receive = {

        // Search Pattern; Denotes a request to make a query for Repos of the provided language
        // This will return a response to the master
        case GitSearch(lang) => {

//            val url = s"https://api.github.com/search/repositories?q=language:$lang+user:tobami+repo:littlechef+size:2344&sort=stars&order=desc"
            val url = s"https://api.github.com/search/repositories?q=language:$lang+size:3000&sort=stars&order=desc"
            println(s"\n${self.path.name}: -> ${sender.path}\n${sender.path.name}: Gave the Search signal. Making request for: $url")

            supervisor = sender
            http.singleRequest(HttpRequest(uri = url, headers = List(authorize)))
                    .pipeTo( context.actorOf(Props[JsonActor], name = "jsonSearch") )
        }


        case GitCommit(fullName) => {
            val url = s"https://api.github.com/repos/$fullName/contents"
            supervisor = sender
            http.singleRequest(HttpRequest(uri = url, headers = List(authorize)))
                    .pipeTo( context.actorOf(Props[JsonActor], name = "jsonSearch") )
        }


        // Get the resultant JSON from the child JsonActor. Stop the child actor and pass along the
        // resultant json data to the Supervisor
        case JsonResult(json) => {
            println (s"\n${self.path.name}:->${sender.path.name} sent us some $json")
            sender() ! PoisonPill
            supervisor ! JsonResult(json)
        }

        case MyMessage(m) => {
            println(s"${self.toString}:I received the message: $m")
            sender() ! Done(s"I got your message! -${self.toString} ")
        }

        case Done(m) => println(s"${self.toString}: $m")

    }

}
