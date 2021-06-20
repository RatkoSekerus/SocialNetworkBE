package auth
import java.time.Clock
import com.auth0.jwk.UrlJwkProvider
import javax.inject.Inject
import pdi.jwt.{JwtAlgorithm, JwtBase64, JwtClaim, JwtJson}
import play.api.Configuration
import scala.util.{Failure, Success, Try}
import models.User

class AuthService @Inject()(config: Configuration) {

  implicit val clock: Clock = Clock.systemUTC

  val secretKey      = "secret key"
  val algo           = JwtAlgorithm.HS256

  def encode(user: User): String = {
    val claim = JwtClaim(
      issuer = Some("socialNetwork"),
      subject = Some(user.userName+" "+user.password)
    ).expiresIn(3600 * 24)
    JwtJson.encode(claim, secretKey, algo)
  }

  def decode(token: String): Any = {

    JwtJson.decode(token, secretKey, Seq(algo)) match {

      case Success(claim) => claim
      case Failure(_) => None
    }
  }

}