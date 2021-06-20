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
import repository.LikeTableDef
import models.Likes
import models.User
import scala.concurrent.{ExecutionContext, Future}



class LikesQ @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                       (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  // the HasDatabaseConfigProvider trait gives access to the
  // dbConfig object that we need to run the slick queries

  val likes = TableQuery[LikeTableDef]

  def add(like: Likes): Future[String] = {
    dbConfig.db.run(likes += like).map(res => "Like successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
      }
  }

  def check(userID: Long, postID: Long): Future[Option[Likes]] = {
    dbConfig.db.run(likes.filter
    (like=>(like.userID===userID && like.postID === postID)).result.headOption)

  }

  def delete(userID: Long, postID: Long): Future[Int] = {
    dbConfig.db.run(likes.filter(like => (like.userID === userID && like.postID === postID)).delete)
  }

  def get(id: Long): Future[Option[Likes]] = {
    dbConfig.db.run(likes.filter(_.likeID === id).result.headOption)
  }

  def listAll: Future[Seq[Likes]] = {
    dbConfig.db.run(likes.result)
  }
  def listLikes(postID:Long): Future[Seq[Likes]] = {
    dbConfig.db.run(likes.filter(_.postID === postID).result)
  }

}