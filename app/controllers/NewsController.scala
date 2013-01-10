package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import anorm._
import models._
import models.news.News
import views._

import securesocial.core._

object NewsController extends Controller with SecureSocial {

  /**
   * Display the news dashboard.
   */
  def index = SecuredAction { implicit request =>
    Ok(
      html.News.index(
        "Index of News controller",
        News.all,
        request.user
      )
    )
  }

  /**
   * News Form definition.
   */
  val newsForm: Form[News] = Form(
    mapping(
      "title" -> nonEmptyText,
      "content" -> nonEmptyText,
      "imageUrl" -> nonEmptyText
    )(News.apply)(News.unapply)
    {
      // Binding: Create a News from the mapping result
      (title, content, imageUrl) => News(title, content, imageUrl)
    }
    {
      // Unbinding: Create the mapping values from an existing News value
      news => Some(news.title, news.content, news.imageUrl)
    }
  )

  /**
   * Display an empty form.
   */
  def create = SecuredAction { implicit request =>
    Ok(html.News.form(newsForm))
  }

  /**
   * Display a form pre-filled with an existing News.
   */
  def edit = SecuredAction { implicit request =>
    val existingNews = News("Some Title", "Some Content", "Some Url")
    Ok(html.News.form(newsForm.fill(existingNews)))
  }

  /**
   * Handle form submission.
   */
  def submit = SecuredAction { implicit request =>
    newsForm.bindFromRequest.fold(
      errors => BadRequest(html.News.form(errors)),
      contact => Ok(html.News.index("Some Title", News.all, request.user))
    )
  }

}
