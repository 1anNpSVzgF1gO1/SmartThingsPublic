/**
 *  Test SmartApp
 *
 *  Copyright 2018 1anNpSVzgF1gO1
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Water Filter Monitor",
    namespace: "1anNpSVzgF1gO1",
    author: "1anNpSVzgF1gO1",
    description: "Test SmartApp",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    section("Turn off when power is detected") {
        input "theWattage", "capability.powerMeter", required: true, title: "Where?"
    }
    section("Turn off this light") {
        input "theswitch", "capability.switch", required: true
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
    subscribe(theWattage, "power", handlePowerEvent)
}

def handlePowerEvent(evt) {
    sendValue(evt) { it.toString() }
}

private sendValue(evt, Closure convert) {
    def compId = URLEncoder.encode(evt.displayName.trim())
    def streamId = evt.name
    def value = convert(evt.value)

	def now = new Date()
	def date = URLEncoder.encode(now.format("MM/dd/yyyy HH:mm:ss", TimeZone.getTimeZone("America/New_York")))

    def params = [
        uri: "https://script.google.com/macros/s/AKfycbwqL97tUvWiXwu4XcltzZ8AqjkvT5N1_yyvZeKT62fxGmLxPnQ/exec?Date=${date}&Power=${value}&App=WaterFilter"
    ]

    try {
        httpGet(params) { resp ->
            resp.headers.each {
//            log.debug "${it.name} : ${it.value}"
        }
//        log.debug "response contentType: ${resp.contentType}"
        log.debug "response data: ${resp.data}"
        }
    } catch (e) {
        log.error "something went wrong: $e"
    }

}