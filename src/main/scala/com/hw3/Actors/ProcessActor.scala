package com.hw3.Actors

import akka.actor.Actor
import com.hw3.Patterns.Messages._
import com.hw3.Utils._
import scala.concurrent._

import scala.sys.process._

/**
  * Created by krbalmryde on 11/5/16.
  */
class ProcessActor extends Actor {

    import akka.pattern.pipe
    import context.dispatcher

    val resourceDir = s"$pwd/src/main/resources"

    def receive = {


        case CloneRepo(id, name, url) => {

            Future {
                CloneResult(
                    s"git clone $url $resourceDir/$id/$name"
                            .run
                            .exitValue()
                )
            }.pipeTo(sender)
        }


        case Foo(id, name, url, lang) => {
            println("\nWe got Foo somehow...\n")
            val success = {
                val outFile = s"$resourceDir/$id/$name.udb"
                val language = lang
                val sourceDir = s"$$resourceDir/$id/$name"

                s"echo $pwd $url $id"
                        .#&&(s"git clone $url $resourceDir/$id/$name")
                        .#&&(s"und -db $outFile create -languages $language add $sourceDir analyze").!
            }
            sender ! CloneResult(success)
        }


        // Delete the Repository
        case CleanRepo(id, name) => {
            println(s"${sender.path.name} asked me to remove $resourceDir/$id/$name")
            Future {
                CleanResult(
                    s"rm -r $resourceDir/$id/$name".run.exitValue
                )
            }.pipeTo(sender)
        }


        case UDB(id, name, lang) => {
            println("generate a .udb file")
            val outFile = s"$resourceDir/$id/$name.udb"
            val sourceDir = s"$resourceDir/$id/$name"
            s"echo generating $resourceDir/$id/$name.udb"
            Future {
                UDBResult(
                    s"und -db $outFile create -languages $lang add $sourceDir analyze".run.exitValue
                )
            }.pipeTo(sender)
        }
    }
}
