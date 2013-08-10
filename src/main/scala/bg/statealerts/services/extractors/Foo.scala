package bg.statealerts.services.extractors

import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage
import java.util.ArrayList
import com.gargoylesoftware.htmlunit.html.HtmlElement
import scala.collection.mutable.Buffer
import scala.collection.JavaConversions
import com.gargoylesoftware.htmlunit.WebRequest
import com.gargoylesoftware.htmlunit.HttpMethod
import java.net.URL
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures
import com.gargoylesoftware.htmlunit.BrowserVersion
import java.io.{ InputStream, FileInputStream, File }
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper
import org.apache.lucene.index.{ DirectoryReader, IndexReader }
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.search.IndexSearcher
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat
import java.util.regex.Pattern

object Foo {
  def main(args: Array[String]) {
    val str = " Брой 50, 7.6.2013 г."
    val regex = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(str)
    if (matcher.find()) {
      println(matcher.group())
    }
  }

  def maina(args: Array[String]) {
    val bvf: Array[BrowserVersionFeatures] = new Array[BrowserVersionFeatures](1)
    bvf(0) = BrowserVersionFeatures.HTMLIFRAME_IGNORE_SELFCLOSING
    val bv: BrowserVersion = new BrowserVersion(BrowserVersion.FIREFOX_17.getApplicationName(),
      BrowserVersion.FIREFOX_17.getApplicationVersion(),
      BrowserVersion.FIREFOX_17.getUserAgent(),
      BrowserVersion.FIREFOX_17.getBrowserVersionNumeric(),
      bvf);
    val client: WebClient = new WebClient(bv)

    client.setJavaScriptEnabled(false)
    //val htmlPage: HtmlPage = client.getPage("file:///C:/Users/bozho/Desktop/bills.html");
    val url = "http://www.parliament.bg/bg/bills"
    val httpMethod = "POST"
    val request: WebRequest = new WebRequest(new URL(url), HttpMethod.valueOf(httpMethod));
    // POST parameters are set in the request body
    if (HttpMethod.valueOf(httpMethod) == HttpMethod.POST) {
      request.setRequestBody("from=&to=&L_ActL_title=&L_Ses_id=&L_Act_sign=&L_Act_im_id=&A_ns_C_id=&submit=%D0%A2%D1%8A%D1%80%D1%81%D0%B8")
    }
    val htmlPage: HtmlPage = client.getPage(request);
    println(htmlPage.getWebResponse.getContentAsString)

    var list: ArrayList[HtmlElement] = htmlPage.getByXPath("//table[@class='billsresult']/tbody/tr/td[4]").asInstanceOf[ArrayList[HtmlElement]]
    var buff: Buffer[HtmlElement] = JavaConversions.asScalaBuffer(list)
    for (element <- buff) {
      println(element.getTextContent())
    }
  }
}