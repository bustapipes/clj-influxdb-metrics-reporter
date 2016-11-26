; Copyright 2016 Busta Pipes
; This file is part of clj-influxdb-metrics-reporter
;
; clj-influxdb-metrics-reporter is free software: you can redistribute it and/or modify
; it under the terms of the GNU General Public License as published by
; the Free Software Foundation, either version 3 of the License, or
; (at your option) any later version.
;
; clj-influxdb-metrics-reporter is distributed in the hope that it will be useful,
; but WITHOUT ANY WARRANTY; without even the implied warranty of
; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
; GNU General Public License for more details.
;
; You should have received a copy of the GNU General Public License
; along with clj-influxdb-metrics-reporter  If not, see <http://www.gnu.org/licenses/>.
(defproject clj-influxdb-metrics-reporter "0.0.1-SNAPSHOT"
  :description "Report metrics to Influx DB"
  :url "https://github.com/bustapipes/clj-influxdb-metrics-reporter"
  :license {:name "Lesser General Public License"
            :url "http://www.gnu.org/licenses/lgpl.html"}
  :dependencies [[http-kit "2.2.0"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]]
  :main ^:skip-aot clj-influxdb-metrics-reporter.core
  :target-path "target/%s"
  :profiles {:test {:dependencies [[midje "1.7.0"]]
                    :plugins [[lein-midje "3.1.3"]]}
             :uberjar {:aot :all}}
  :aliases {"repl-test" ["with-profile" "+test" "repl"]
            "test" ["with-profile" "+test" "midje"]})
