package com.hw3.Actors

import akka.actor.{Actor, PoisonPill}
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

//    val resourceDir = s"$pwd/src/main/resources"
    val resourceDir = s"$pwd/KrbAlmryde_Data/resources"

    def receive = {

        // Clones provided url to $resourceDir/$id/$name
        case CloneRepo(id, name, url) => {
            //                                     $resourceDir/$id/$name"
            println(s"${self.path} cloning $url to $resourceDir/$id/$name")
            Future {
                CloneResult(
                    s"git clone $url $resourceDir/$id/$name".run.exitValue()
                )
            }.pipeTo(sender)
        }

        // Generate Understand Database
        case GenUDB(id, name, lang) => {
            println("generate a .udb file")
            val outFile = s"$resourceDir/$id/$name.udb"
            val sourceDir = s"$resourceDir/$id/$name"
            val stdout = new StringBuilder
            val stderr = new StringBuilder
            println(s"generating $resourceDir/$id/$name.udb")
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
            println("I am making a patch!")
            val patchCMD = s"git format-patch --summary --numstat --numbered-files --ignore-blank-lines --no-binary -1 HEAD -o $resourceDir/$id"
            Future {
                PatchResult(
                    s"cd $resourceDir/$id/$name".#&&( patchCMD ).run.exitValue,
                    id
                )
            }.pipeTo(sender)
        }


        /*********************
         *  CLEAN UP MESSAGES
         *********************/
        // Delete the Repository
        case CleanRepo(id) => {
            println(s"${sender.path.name} asked me to remove $resourceDir/$id")
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
