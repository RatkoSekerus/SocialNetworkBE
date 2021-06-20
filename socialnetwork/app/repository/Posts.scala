package repository

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import repository.PostTableDef
import slick.jdbc.MySQLProfile.api._
import play.api.libs.json._
import slick.jdbc.MySQLProfile.ColumnOptions
import models.Post
import scala.concurrent.{ExecutionContext, Future}

class Posts @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                      (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  // the HasDatabaseConfigProvider trait gives access to the
  // dbConfig object that we need to run the slick queries

  val posts = TableQuery[PostTableDef]

  def add(post: Post): Future[String] = {

    dbConfig.db.run(posts += post).map(res => "Post successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(postID: Long,userID: Long): Future[Int] = {
    dbConfig.db.run(posts.filter(post=>(post.postID===postID && post.userID===userID)).delete)
  }

  def get(id: Long): Future[Option[Post]] = {
    dbConfig.db.run(posts.filter(_.postID === id).result.headOption)
  }

  def listAll: Future[Seq[Post]] = {
    dbConfig.db.run(posts.result)
  }

}