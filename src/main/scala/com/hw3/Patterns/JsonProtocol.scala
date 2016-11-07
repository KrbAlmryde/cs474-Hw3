package com.hw3.Patterns

import org.json4s.DefaultFormats

/**
  * Created by krbalmryde on 11/5/16.
  */

/**
  * The following are a series of Case Classes which assist in parsing JSON data
  */


object JsonProtocol {
    implicit val formats = DefaultFormats // Brings in default date formats etc.

    case class URL(path:Option[String])
    case class RepoDetails(name:String, url:String, id:String, lang:String) // Simple example
    case class CloneURL(clone_url:String)

    case class SearchResult(
            total_count: BigInt,
            incomplete_results:Boolean,
            items:List[Repo]
    )

    // Unmarshalling pattern for extrating Github Results
    case class Owner(
            gists_url: Option[String],
            organizations_url: Option[String],
            gravatar_id: Option[String],
            url: Option[String],
            repos_url: Option[String],
            received_events_url: Option[String],
            id: BigInt,
            following_url: Option[String],
            site_admin: Boolean,
            subscriptions_url: Option[String],
            starred_url: Option[String],
            html_url: Option[String],
            login: Option[String],
            `type`: Option[String],
            events_url: Option[String],
            avatar_url: Option[String],
            followers_url: URL
    )

    case class Repo(
           tags_url: Option[String],
           statuses_url: Option[String],
           has_downloads: Boolean,
           blobs_url: Option[String],
           git_refs_url: Option[String],
           issue_events_url: Option[String],
           name: Option[String],
           has_issues: Boolean,
           watchers_count: BigInt,
           forks: BigInt,
           `private`: Boolean,
           size: BigInt,
           open_issues_count: BigInt,
           open_issues: BigInt,
           subscribers_url: Option[String],
           stargazers_count: BigInt,
           url: Option[String],
           full_name: Option[String],
           releases_url: Option[String],
           description: Option[String],
           trees_url: Option[String],
           branches_url: Option[String],
           score: Double,
           pushed_at: Option[String],
           git_url: Option[String],
           collaborators_url: Option[String],
           mirror_url: Option[String],
           subscription_url: Option[String],
           languages_url: Option[String],
           has_wiki: Boolean,
           commits_url: Option[String],
           contents_url: Option[String],
           fork: Boolean,
           git_tags_url: Option[String],
           downloads_url: Option[String],
           svn_url: Option[String],
           milestones_url: Option[String],
           id: BigInt,
           language: Option[String],
           compare_url: Option[String],
           notifications_url: Option[String],
           comments_url: Option[String],
           pulls_url: Option[String],
           teams_url: Option[String],
           forks_count: BigInt,
           merges_url: Option[String],
           keys_url: Option[String],
           deployments_url: Option[String],
           homepage: Option[String],
           contributors_url: Option[String],
           forks_url: Option[String],
           clone_url: Option[String],
           created_at: Option[String],
           hooks_url: Option[String],
           ssh_url: Option[String],
           owner: Owner,
           html_url: Option[String],
           archive_url: Option[String],
           default_branch: Option[String],
           updated_at: Option[String],
           issues_url: Option[String],
           assignees_url: Option[String],
           events_url: Option[String],
           watchers: BigInt,
           issue_comment_url: Option[String],
           labels_url: Option[String],
           has_pages: Boolean,
           git_commits_url: Option[String],
           stargazers_url: Option[String]
    )

}


