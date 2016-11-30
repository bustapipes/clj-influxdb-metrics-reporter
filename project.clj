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
(defproject com.github.bustapipes/clj-influxdb-metrics-reporter "0.0.2-SNAPSHOT"
  :description "Report metrics to Influx DB"
  :url "https://github.com/bustapipes/clj-influxdb-metrics-reporter"
  :license {:name "Lesser General Public License"
            :url "http://www.gnu.org/licenses/lgpl.html"}
  :dependencies [[http-kit "2.2.0"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]]
  :main ^:skip-aot clj-influxdb-metrics-reporter.core
  :target-path "target/%s"
  :profiles {:test {:dependencies [[midje "1.8.3"]]
                    :plugins [[lein-midje "3.2.1"]]}
             :uberjar {:aot :all}}
  :deploy-repositories [["releases" {:url "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                                     :creds :gpg}
                         "snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/"
                                      :creds :gpg}]]
  :scm {:name "git"
        :url "git@github.com:bustapipes/clj-influxdb-metrics-reporter.git"}
  :pom-addition [:developers [:developer
                              [:name "Busta Pipes"]
                              [:url "https://github.com/bustapipes"]
                              [:email "bustapipes@gmail.com"]
                              [:timezone "0"]]]
  :aliases {"repl-test" ["with-profile" "+test" "repl"]
            "test" ["with-profile" "+test" "midje"]})
