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
;(ns-unmap *ns* 'influx-value)
(ns clj-influxdb-metrics-reporter.core-test
  (:require [clojure.test :refer :all]
            [clj-influxdb-metrics-reporter.core :refer :all]
            [midje.sweet :refer :all]
            [midje.util :as mu]))

(mu/testable-privates clj-influxdb-metrics-reporter.core specify-default influx-tagger)

(fact "core reporting"
      (influx-tagger "a.b.c") => "c,a=b"
      (influx-tagger "a.b.d.e.c") => "c,a=b,d=e"
      (influx-tagger "a.b.d.e.f.c") => "c,a=b,d=e"
      (reporter nil) => {:registry nil}
      (transport {} nil) => {:influx-db nil}
      (specify-default {} :a 0) => 0
      (specify-default {:a 1} :a 0) => 1
      (-> (start {}) deref) => nil
      (provided (#'clj-influxdb-metrics-reporter.core/connect anything) => nil)
      (provided (#'clj-influxdb-metrics-reporter.core/report-loop anything) => nil))
