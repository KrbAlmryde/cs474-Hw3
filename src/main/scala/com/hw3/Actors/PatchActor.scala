package com.hw3.Actors

import java.io.File

import akka.actor.Actor.Receive
import akka.actor.{Actor, PoisonPill, Props}
import com.hw3.Patterns.Messages.{FinalOutput, GenPatch, PatchResult}

import scala.concurrent.Future
import scala.io.Source
import scala.util.matching.Regex
/**
  * Created by krbalmryde on 11/12/16.
  */
class PatchActor extends Actor {

    val resourceDir = s"repositories"
    var supervisor = self

    def receive: Receive = {


        case GenPatch(id, name) => {
            supervisor = sender
            val prActor = context.actorOf(Props[ProcessActor], name="patchMaker")
            prActor ! GenPatch(id, name)
        }


        // If weve made it this far it means weve managed to create some kind of patch file
        case PatchResult(0, id) => {
            println (s"${self.path.name}: Patch made it!!")

            val patchFile = s"$resourceDir/$id/1"

            if ( new File(patchFile).exists ) {
                println(s"\t${self.path.name}: Great news, it exists too!")
                val results = processPatch( Source.fromFile(patchFile).getLines )
                println(s"\tthere were ${results.size} files changed" )
//                println(s"\tthere were ${results.size} files changed" )
                if (results.nonEmpty) {
                    val top = results.sortWith(_._2 > _._2)
                    val bottom = results.sortWith(_._1 > _._1)

                    val N = results.size
                    val W = top(0)._3
                    val X = top(0)._2
                    val Y = bottom(0)._3
                    val Z = bottom(0)._1
                    val result = new StringBuilder
                    result.append(s"Final Report for Repo:$id\n")
                    result.append(s"\t$N changes were made")
                    result.append(s"\tSuggest developer retest Module $W due to $X changes made\n")
                    result.append(s"\tSuggest developer retest Module $Y due to $Z changes made\n\n")
                    println(result.toString)

                    supervisor ! FinalOutput(result.toString)
                } else
                    println("\tSorry, maybe the next commit? :-) ")

            } else
                println("File not Found! Bummer...")
        }

        case _ => sender ! PoisonPill
    }


    def processPatch(contents: Iterator[String]): List[(Int, Int, String)] = {

        val data = {
            contents.filter(line => {
                val pattern = raw"""\d+\t\d+\t*(\w|/)+.(frag|vert|scala|java|xml|cfg)""".r
                pattern.findFirstIn(line).isDefined
            })
            .map(line => {
                line.split("\t") match {
                    case xyz:Array[String] => {
                        val tupple = (xyz(0).toInt, xyz(1).toInt, xyz(2))
                        tupple
                    } // We want them to be tuples
                }
            }).toList
        }
        data

    }
}
