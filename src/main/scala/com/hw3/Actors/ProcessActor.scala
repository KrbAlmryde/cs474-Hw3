package com.hw3.Actors

import akka.actor.Actor
import com.hw3.Patterns.Messages._
import com.hw3.Utils._

import scala.sys.process._

/**
  * Created by krbalmryde on 11/5/16.
  */
class ProcessActor extends Actor {

    val resourceDir = s"$pwd/src/main/resources"

    def receive = {
        case CloneRepo(id, url, path) => {
            val success = {
                s"echo $pwd $url $id"
                        .#&&(s"git clone $url $resourceDir/$id/$path").!
            }
            sender ! CloneResult(success)

        }

        // Delete the Repository
        case CleanRepo(name, id) => {
            val success = {
                s"echo removing $resourceDir/$id/$name"
                        .#&&(s"rm -r $resourceDir/$id/$name").!
            }
            sender ! CloneResult(success)
        }


        case UDB(id, lang, name) => {
            println("generate a .udb file")
            val outFile = s"$resourceDir/$id/$name.udb"
            val language = lang
            val sourceDir = s"$$resourceDir/$id/$name"
            val success = {
                s"echo generating $resourceDir/$id/$name.udb"
                        .#&&(s"und -db $outFile create -languages $language add $sourceDir analyze").!
            }
            sender ! UDBResult(success)
        }
    }
}
