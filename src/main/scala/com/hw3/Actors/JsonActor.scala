package com.hw3.Actors

import akka.actor.{Actor, PoisonPill, Props}
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import com.hw3.Patterns.Messages.{Done, JsonResult, MyMessage}
import org.json4s.JsonAST.JValue
import org.json4s._
import org.json4s.native.JsonMethods._


/**
  * Created by krbalmryde on 11/1/16.
  *
  * Handles HTTP Responses extracting Json and sending it back to its Supervisor.
  * Supervisor should terminate the actor when it has completed its operation
  */

class JsonActor extends Actor {
    import akka.pattern.pipe
    import context.dispatcher

    // Implicits
    implicit val formats = DefaultFormats // Brings in default date formats etc.
    final implicit val materializer:ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))


    def receive = {
        // Receive the Request Json, time to do some parsing!


        case HttpResponse(StatusCodes.OK, headers, entity, _) => {
            println(s"\n${self.path}\n${self.path.name}: Request piped through successfully! ${entity.contentType}\n\tfrom ${sender.path.name}")

            // Extract the content string type to Json and pipe it over to the JsonHandling Actor
            entity.dataBytes
                    .runFold(ByteString(""))(_++_)
                    .map(body => JsonResult(parse(body.utf8String)) )
                    .pipeTo( sender() ) // send to GithubApiActor (usually)
        }

        case resp @ HttpResponse(code, _, _, _) => {
            println("\nRequest failed, response code: " + code)
            resp.discardEntityBytes()
        }


        /* Used for Debugging Actors */
        case MyMessage(m) => {
            println(s"${self.path.name}:I received the message: $m")
            sender() ! Done(s"I got your message! -${self.path.name} ")
        }

        case Done(m) => println(s"${self.path.name}: $m")

        case _ => println(s"${self.path.name}: Hell if I know what it was?")

    }

}
