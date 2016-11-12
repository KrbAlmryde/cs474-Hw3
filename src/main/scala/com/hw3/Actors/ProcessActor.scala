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

    def receive = {

        // Clones provided url to $resourceDir/$id/$name
        case CloneRepo(id, name, url) => {
            Future {
                CloneResult(
                    s"git clone $url $resourceDir/$id/$name".run.exitValue()
                )
            }.pipeTo(sender)
        }

        // Generate Understand Database
        case GenUDB(id, name, lang) => {
            val outFile = s"$resourceDir/$id/$name.udb"
            val sourceDir = s"$resourceDir/$id/$name"
            val stdout = new StringBuilder
            val stderr = new StringBuilder
//            println(s"generating $resourceDir/$id/$name.udb")
            Future {
                UDBResult(
                    s"und -db $outFile create -languages $lang add $sourceDir analyze".run( ProcessLogger(stdout append _, stderr append _) ).exitValue,
                    id,
                    name
                )
            }.pipeTo(sender)
        }

        // Generates our patch!
        case GenPatch(id, name) => {
            Future {
                PatchResult(
                    sys.process.Process(
                        Seq("git", "format-patch", "--summary", "--numstat", "--numbered-files", "--ignore-blank-lines", "--no-binary", "-1", "HEAD", "-o", s"$resourceDir/$id"),
                        new java.io.File(s"$resourceDir/$id/$name")
                    ).run.exitValue,
                    id
                )
            }.pipeTo(sender)
        }


        /*********************
         *  CLEAN UP MESSAGES
         *********************/
        // Delete the Repository
        case CleanRepo(id) => {
            Future {
                CleanRepoResult(
                    s"rm -rf $resourceDir/$id".run.exitValue
                )
            }.pipeTo(sender)
        }

        // Delete the .UDB file
        case CleanUDB(id, name) => {
            println(s"${sender.path.name} asked me to remove $resourceDir/$id/$name.udb")
            Future {
                CleanUDBResult(
                    s"rm -r $resourceDir/$id/$name.udb".run.exitValue
                )
            }.pipeTo(sender)
        }

    }
}
