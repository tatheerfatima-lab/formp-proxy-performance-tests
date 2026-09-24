/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.perftests.simulation

import io.gatling.core.action.builder.ActionBuilder
import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.AuthLogin.AuthLoginRequests._
import uk.gov.hmrc.perftests.BaseRequests
import uk.gov.hmrc.perftests.sdlt.SdltFormpRequests._

trait SDLTFormpAPI extends PerformanceTestRunner with BaseRequests {
  val sdltFormP: List[ActionBuilder] =
    getAuthToken(authPayloadSdlt) ++
      List[ActionBuilder](
        postSdltReturns,
        getSDLTReturns,
        postGovtalkStatus,
        getGovtalkStatus
      )


  setup(
    "sdlt-get-post-formp",
    "SDLT GET and POST returns Formp APIs"
  ) withActions (
    sdltFormP: _*
    )

}
