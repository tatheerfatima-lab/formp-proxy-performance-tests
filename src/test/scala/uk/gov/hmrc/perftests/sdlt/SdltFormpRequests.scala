/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.sdlt

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import uk.gov.hmrc.perftests.BaseRequests

import scala.util.Random

object SdltFormpRequests extends ServicesConfiguration with BaseRequests {

  val postReturns           = s"$formpUrl/create/return"
  val getReturns            = s"$formpUrl/retrieve-return"
  val createPurchaser       = s"$formpUrl/filing/create/purchaser"
  val govtalkStatus         = s"$formpUrl/filing/govtalk-status"

  def commonHeaders: Map[CharSequence, String] = Map(
    HttpHeaderNames.Authorization -> s"#{bearerToken}",
    HttpHeaderNames.ContentType   -> "application/json",
    "X-Session-ID"                -> "693b2579c9ae70489252dba5"
  )

  val stornId = "STORN12345"
  val returnResourceRef = "221"
  val userIdentifier = s"USER${Random.nextLong(999999999L)}"
  val formResultId = s"FRID-${Random.nextLong(999999999L)}"

  val postSdltReturns: HttpRequestBuilder =
    http("POST returns for SDLT")
      .post { _ =>
        val requestUrl = postReturns
        requestUrl
      }
      .headers(commonHeaders)
      .body(
        StringBody(
          s"""{
             |"stornId": "$stornId",
             |"purchaserIsCompany": "YES",
             |"surNameOrCompanyName": "ABC Property Ltd",
             |"houseNumber": 100,
             |"addressLine1": "Business Park",
             |"transactionType": "NON_RESIDENTIAL"
             |}""".stripMargin
        )
      )
      .asJson
      .check(status.is(201))

  val getSDLTReturns: HttpRequestBuilder =
    http("GET returns for SDLT")
      .post { _ =>
        val requestUrl = getReturns
        requestUrl
      }
      .headers(commonHeaders)
      .body(
        StringBody(
          s"""{
             |"storn": "$stornId",
             |"returnResourceRef": "$returnResourceRef"
             |}""".stripMargin
        )
      )
      .asJson
      .check(status.is(200),
        jsonPath("$.returnResourceRef").exists
      )


  val postGovtalkStatus: HttpRequestBuilder =
    http("Post govtalk status for SDLT")
      .post { _ =>
        val requestUrl = govtalkStatus
        requestUrl
      }
      .headers(commonHeaders)
      .body(
        StringBody(
          s"""{
             |"userIdentifier": "$userIdentifier",
             |"formResultId" : "$formResultId",
             |"correlationId" : "CORR-1",
             |"govTalkStatus" : {
             |    "formLock" : "0",
             |    "createTimestamp" : "2026-01-01 10:00:00",
             |    "endStateTimestamp" : null,
             |    "lastMessageTimestamp" : "2026-01-01 10:05:00",
             |    "numberOfPolls" : "0",
             |    "pollInterval" : "10",
             |    "protocolStatus" : "SUBMITTED",
             |    "gatewayUrl" : "https://transaction-engine.example/submission"
             |     }
             |}""".stripMargin
        )
      )
      .asJson
      .check(status.is(201)
      )

  val getGovtalkStatus: HttpRequestBuilder =
    http("Get govtalk status for SDLT")
      .get(govtalkStatus)
      .headers(commonHeaders)
      .body(
        StringBody(
          s"""{
             |"userIdentifier": "$userIdentifier",
             |"formResultId" : "$formResultId"
             |}""".stripMargin
        )
      )
      .asJson
      .check(status.is(200)
      )
}
